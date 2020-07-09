package com.leeroy.forwordpanel.forwordpanel.common.util;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

@Slf4j
public class SimpleForward extends Thread {
    // 服务器
    private ServerSocket server;
    // 监听本地端口
    private int localPort = 1080;
    // 目标主机地址
    private String remoteHostAddr = "0.0.0.0";
    // 目标主机端口
    private int remoteHostPort = 8388;
    // 设置超时时间 30s
    private static int TIMEOUT = 30;
    // 结束标志
    volatile boolean runFlag = true;
    // 客户端列表 用于删除失效连接和超时连接
    private static HashMap<Socket, Date> clientList = new HashMap<>();

    public static void main(String[] args) {
        new SimpleForward(1234, "143.92.44.202", 443);
    }


    public SimpleForward(int localPort, String remoteHostAddr, int remoteHostPort) {
        this.localPort = localPort;
        this.remoteHostAddr = remoteHostAddr;
        this.remoteHostPort = remoteHostPort;
    }

    public void stopForward(){
        try {
            server.close();
        } catch (IOException e) {
            log.error("close error", e);
        }
        this.runFlag = false;
    }

    @Override
    public void run() {
        try {
            this.server = new ServerSocket(this.localPort);
            log.info("服务器开启成功");
            log.info("监听端口 : " + this.localPort);
        } catch (IOException e) {
            log.info("服务器开启失败");
            log.info(e.getMessage());
            log.info("退出运行");
            return;
        }
        new Thread(new AutoDestroy()).start();
        while (runFlag) {
            Socket socket = null;
            Socket remoteHost = null;
            try {
                socket = server.accept();
                // 接收到请求就把socket扔进map,value为刷新时间
                clientList.put(socket, new Date());
                String address = socket.getRemoteSocketAddress().toString();
                log.debug("新连接 ： " + address);
                // 建立与目标主机的连接
                remoteHost = new Socket(this.remoteHostAddr, this.remoteHostPort);
                log.debug("连接地址 : " + this.remoteHostAddr + ":" + this.remoteHostPort);
                // 端口转发
                new Thread(new Switch(socket, remoteHost, remoteHost.getInputStream(), socket.getOutputStream())).start();
                new Thread(new Switch(socket, remoteHost, socket.getInputStream(), remoteHost.getOutputStream())).start();
            } catch (IOException e) {
                log.info("连接异常");
                log.info(e.getMessage());
                close(socket);
                close(remoteHost);
            }
        }
        log.info("closed...");
    }

    private void close(Socket socket) {
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 用于端口转发
    private class Switch implements Runnable {
        private Socket host;
        private Socket remoteHost;
        private InputStream in;
        private OutputStream out;

        Switch(Socket host, Socket remoteHost, InputStream in, OutputStream out) {
            this.host = host;
            this.remoteHost = remoteHost;
            this.in = in;
            this.out = out;
        }

        @Override
        public void run() {
            int length;
            byte[] buffer = new byte[1024];
            try {
                while (!host.isClosed() && (length = in.read(buffer)) > -1) {
                    clientList.put(host, new Date());
                    out.write(buffer, 0, length);
                }
            } catch (IOException e) {
                log.debug("连接关闭");
            } finally {
                close(host);
                close(remoteHost);
            }
        }
    }

    // 用于清除失效连接和超时连接
    private class AutoDestroy implements Runnable {
        @Override
        public void run() {
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    List<Socket> list = new LinkedList<>();
                    log.info("开始扫描失效与超时连接");
                    Date start = new Date();
                    for (Socket socket : clientList.keySet()) {
                        Date lastTime = clientList.get(socket);
                        long time = new Date().getTime() - lastTime.getTime();
                        if (socket.isClosed() || time / 1000 >= TIMEOUT) {
                            list.add(socket);
                        }
                    }
                    log.info("找到" + list.size() + "个,用时 : " + (new Date().getTime() - start.getTime()) + "毫秒");
                    log.info("开始清除失效与超时连接");
                    for (Socket socket : list) {
                        try {
                            clientList.remove(socket);
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    log.info("当前连接数 : " + clientList.size());
                }
            }, 30 * 1000, 30 * 1000);
        }
    }

//    private class Terminal implements Runnable {
//        private String format = "yyyy-MM-dd HH:mm:ss";
//        private SimpleDateFormat dateFormat = new SimpleDateFormat(format);
//
//        @Override
//        public void run() {
//            while (!server.isClosed()) {
//                System.out.print("请输入命令 : ");
//                String cmd = reader.readKB();
//                handler(cmd);
//            }
//        }
//
//        private void handler(String cmd) {
//            switch (cmd) {
//                case "status":
//                   log.info("当前时间 : " + dateFormat.format(new Date()));
//                   log.info("总连接数 : " + clientList.size());
//                    for (Socket socket : clientList.keySet()) {
//                        long time = new Date().getTime() - clientList.get(socket).getTime();
//                       log.info("<" + socket.getRemoteSocketAddress().toString() + "> " + time / 1000);
//                    }
//                    break;
//            }
//        }
//    }
}
