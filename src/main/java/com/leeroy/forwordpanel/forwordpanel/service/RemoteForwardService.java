package com.leeroy.forwordpanel.forwordpanel.service;

import com.alibaba.fastjson.JSON;
import com.leeroy.forwordpanel.forwordpanel.common.util.remotessh.SSHCommandExecutor;
import com.leeroy.forwordpanel.forwordpanel.model.Server;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class RemoteForwardService {

    private Map<Server, SSHCommandExecutor> sshCommandExecutorMap = new ConcurrentHashMap<>();

    /**
     * 获取sshExecutor
     * @param server
     * @return
     */
    public SSHCommandExecutor getSshExecutor(Server server) {
        SSHCommandExecutor sshCommandExecutor = sshCommandExecutorMap.get(server);
        if(sshCommandExecutor==null){
            sshCommandExecutor = new SSHCommandExecutor(server);
            sshCommandExecutorMap.put(server, sshCommandExecutor);
        }
        return sshCommandExecutor;
    }



    /**
     * 添加转发
     *
     * @param remoteHost
     * @param remotePort
     * @param localPort
     */
    public void addForward(Server server,String remoteHost,  Integer remotePort,  Integer localPort) {
        SSHCommandExecutor sshExecutor = getSshExecutor(server);
        stopForward(server, remoteHost, remotePort, localPort);
        sshExecutor.execute((String.format("ip -o -4 addr list | grep -Ev '\\s(docker|lo)' | awk '{print $4}' | cut -d/ -f1 | grep -Ev '(^127\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}$)|(^10\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}$)|(^172\\.1[6-9]{1}[0-9]{0,1}\\.[0-9]{1,3}\\.[0-9]{1,3}$)|(^172\\.2[0-9]{1}[0-9]{0,1}\\.[0-9]{1,3}\\.[0-9]{1,3}$)|(^172\\.3[0-1]{1}[0-9]{0,1}\\.[0-9]{1,3}\\.[0-9]{1,3}$)|(^192\\.168\\.[0-9]{1,3}\\.[0-9]{1,3}$)'", localPort, remoteHost, remotePort)));
        String localIp = sshExecutor.getResult();
        if(StringUtils.isEmpty(localIp)){
            sshExecutor.execute((String.format("ip -o -4 addr list | grep -Ev '\\s(docker|lo)' | awk '{print $4}' | cut -d/ -f1|head -n 1", localPort, remoteHost, remotePort)));
            localIp = sshExecutor.getResult();
        }
        sshExecutor.execute((String.format("iptables -t nat -A PREROUTING -p tcp --dport %d -j DNAT --to-destination %s:%d", localPort, remoteHost, remotePort)));
        sshExecutor.execute((String.format("iptables -t nat -A PREROUTING -p udp --dport %d -j DNAT --to-destination %s:%d", localPort, remoteHost, remotePort)));
        sshExecutor.execute((String.format("iptables -t nat -A POSTROUTING -p tcp -d %s --dport %d -j SNAT --to-source %s", remoteHost, remotePort, localIp)));
        sshExecutor.execute((String.format("iptables -t nat -A POSTROUTING -p udp -d %s --dport %d -j SNAT --to-source %s", remoteHost, remotePort, localIp)));
        sshExecutor.execute((String.format("iptables -n -v -L -t filter -x | grep %s | awk '{print $2}'", remoteHost)));
        String flow = sshExecutor.getResult();
        log.info("fow: {}", flow);
        if (StringUtils.isEmpty(flow)) {
            sshExecutor.execute((String.format("iptables -I FORWARD -s %s", remoteHost)));
        }
    }


    /**
     * 获取转发流量
     *
     * @param server
     * @param remotePort
     * @return
     */
    public String getPortFlow(Server server, String remoteHost, Integer remotePort) {
        SSHCommandExecutor sshExecutor = getSshExecutor(server);
        if(StringUtils.isEmpty(remoteHost)){
            return "0";
        }
        sshExecutor.execute(String.format("iptables -n -v -L -t filter -x | grep %s | awk '{print $2}'", remoteHost));
        String result = sshExecutor.getResult();
        result = result.replaceAll("\n", "");
        log.info("flow:{}", result);
        return StringUtils.isEmpty(result) ? "0" : result;
    }


    /**
     * 获取转发流量
     *
     * @param remoteHostList
     * @return
     */
    public Map<String, String> getPortFlowMap(Server server, List<String> remoteHostList) {
        SSHCommandExecutor sshExecutor = getSshExecutor(server);
        Map<String, String> resultMap = new HashMap<>();
        Map<String, String> commandList = new HashMap<>();
        for (String remoteHost : remoteHostList) {
            if(StringUtils.isEmpty(remoteHost)){
                resultMap.put(remoteHost, "0");
            }else {
                commandList.put(remoteHost,String.format("iptables -n -v -L -t filter -x | grep %s | awk '{print $2}'", remoteHost));
            }
        }

        sshExecutor.execute(commandList.values().toArray(new String[]{}));
        Vector<String> resultSet = sshExecutor.getResultSet();
        for (int i = 0; i < resultSet.size(); i++) {
            resultMap.put((String) commandList.keySet().toArray()[i],resultSet.get(i));
        }
        return resultMap;
    }


    /**
     * 重置流量
     *
     * @param remoteHost
     * @param remotePort
     */
    public void resetFlowCount(Server server, String remoteHost, Integer remotePort) {
        SSHCommandExecutor sshExecutor = getSshExecutor(server);
        sshExecutor.execute(String.format("iptables -Z FORWARD -p tcp --dport %s -d %s", remotePort, remoteHost));
        sshExecutor.execute(String.format("iptables -Z FORWARD -p udp --dport %s -d %s", remotePort, remoteHost));
    }

    /**
     * 重置流量
     *
     * @param remoteHost
     * @param remotePort
     */
    public void deleteFlowCount(Server server, String remoteHost, Integer remotePort) {
        SSHCommandExecutor sshExecutor = getSshExecutor(server);
        sshExecutor.execute(String.format("iptables -D FORWARD -p tcp --dport %s -d %s", remotePort, remoteHost));
        sshExecutor.execute(String.format("iptables -D FORWARD -p udp --dport %s -d %s", remotePort, remoteHost));
    }


    /**
     * 停止转发
     *
     * @param remoteHost
     * @param remotePort
     * @param localPort
     */
    public void stopForward(Server server, String remoteHost, Integer remotePort, Integer localPort) {
        SSHCommandExecutor sshExecutor = getSshExecutor(server);
        sshExecutor.execute(String.format("iptables -L PREROUTING -n -t nat --line-number |grep DNAT|grep \"dpt:%d \"|sort -r|awk '{print $1,$3,$9}'|tr \" \" \":\"|tr \"\\n\" \" \"", localPort));
        String dnatStr = sshExecutor.getResult();
        log.info(dnatStr);
        dnatStr.trim();
        if (StringUtils.isEmpty(dnatStr)) {
            return;
        }
        String[] dnatList = dnatStr.split(" ");
        log.info(JSON.toJSONString(dnatList));
        for (String item : dnatList) {
            item = item.trim();
            if (StringUtils.isEmpty(item)) {
                continue;
            }
            String[] itemList = item.split(":");
            log.info(JSON.toJSONString(itemList));
            sshExecutor.execute(String.format("iptables -t nat  -D PREROUTING %s", itemList[0]));
            sshExecutor.execute(String.format("iptables -L POSTROUTING -n -t nat --line-number|grep SNAT|grep %s|grep dpt:%s|grep %s|awk  '{print $1}'|sort -r|tr \"\\n\" \" \"", itemList[3], itemList[4], itemList[1]));
            String snatStr = sshExecutor.getResult();
            if (StringUtils.isEmpty(snatStr)) {
                continue;
            }
            snatStr = snatStr.trim();
            if (StringUtils.isEmpty(snatStr)) {
                continue;
            }
            String[] snatList = snatStr.split(" ");
            log.info(JSON.toJSONString(snatList));
            for (String s : snatList) {
                if (StringUtils.isEmpty(s)) {
                    continue;
                }
                sshExecutor.execute("iptables -t nat  -D POSTROUTING " + s);
            }
        }

    }

    private String getForwardKey(String remoteHost, Integer remotePort, Integer localPort) {
        return remoteHost + remotePort + localPort;
    }


    public static void main(String[] args) {
        String a = " ";
        System.out.println();
    }
}
