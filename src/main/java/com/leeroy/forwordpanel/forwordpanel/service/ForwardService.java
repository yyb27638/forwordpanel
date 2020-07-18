package com.leeroy.forwordpanel.forwordpanel.service;

import com.alibaba.fastjson.JSON;
import com.leeroy.forwordpanel.forwordpanel.common.util.IpUtil;
import com.leeroy.forwordpanel.forwordpanel.common.util.ShellUtil;
import com.leeroy.forwordpanel.forwordpanel.dto.DataFlow;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;

@Slf4j
@Service
public class ForwardService {

    /**
     * 添加转发
     *
     * @param remoteHost
     * @param remotePort
     * @param localPort
     */
    public void addForward(String remoteHost, Integer remotePort, Integer localPort) {
        stopForward(remoteHost, remotePort, localPort);
        String localIp = IpUtil.getLocalIpByNetcard();
        ShellUtil.execShell(String.format("iptables -t nat -A PREROUTING -p tcp --dport %d -j DNAT --to-destination %s:%d", localPort, remoteHost, remotePort));
        ShellUtil.execShell(String.format("iptables -t nat -A PREROUTING -p udp --dport %d -j DNAT --to-destination %s:%d", localPort, remoteHost, remotePort));
        ShellUtil.execShell(String.format("iptables -t nat -A POSTROUTING -p tcp -d %s --dport %d -j SNAT --to-source %s", remoteHost, remotePort, localIp));
        ShellUtil.execShell(String.format("iptables -t nat -A POSTROUTING -p udp -d %s --dport %d -j SNAT --to-source %s", remoteHost, remotePort, localIp));
        String flow = ShellUtil.execShell(String.format("iptables -n -v -L -t filter -x | grep %s | awk '{print $2}'", remoteHost));
        log.info("fow: {}", flow);
        if (StringUtils.isEmpty(flow)) {
            ShellUtil.execShell(String.format("iptables -I FORWARD -s %s", remoteHost));
        }
    }


    /**
     * 获取转发流量
     * @param remoteHost
     * @param remotePort
     * @return
     */
    public String getPortFlow(String remoteHost, Integer remotePort) {
        String result = ShellUtil.execShell(String.format("iptables -n -v -L -t filter -x | grep %s | awk '{print $2}'", remoteHost));
        result = result.replaceAll("\n", "");
        log.info("flow:{}", result);
        return StringUtils.isEmpty(result)?"0":result;
    }


    /**
     * 重置流量
     * @param remoteHost
     * @param remotePort
     */
    public void resetFlowCount(String remoteHost, Integer remotePort){
        ShellUtil.execShell(String.format("iptables -Z FORWARD -p tcp --dport %s -d %s",  remotePort, remoteHost));
        ShellUtil.execShell(String.format("iptables -Z FORWARD -p udp --dport %s -d %s",  remotePort, remoteHost));
    }

    /**
     * 重置流量
     * @param remoteHost
     * @param remotePort
     */
    public void deleteFlowCount(String remoteHost, Integer remotePort){
         ShellUtil.execShell(String.format("iptables -D FORWARD -p tcp --dport %s -d %s",  remotePort, remoteHost));
         ShellUtil.execShell(String.format("iptables -D FORWARD -p udp --dport %s -d %s",  remotePort, remoteHost));
    }


    /**
     * 停止转发
     *
     * @param remoteHost
     * @param remotePort
     * @param localPort
     */
    public void stopForward(String remoteHost, Integer remotePort, Integer localPort) {
        String dnatStr = ShellUtil.execShell(String.format("iptables -L PREROUTING -n -t nat --line-number |grep DNAT|grep \"dpt:%d \"|sort -r|awk '{print $1,$3,$9}'|tr \" \" \":\"|tr \"\\n\" \" \"", localPort));
        log.info(dnatStr);
        dnatStr.trim();
        if(StringUtils.isEmpty(dnatStr)){
            return;
        }
        String[] dnatList = dnatStr.split(" ");
        log.info(JSON.toJSONString(dnatList));
        for (String item : dnatList) {
            item = item.trim();
            if(StringUtils.isEmpty(item)){
                continue;
            }
            String[] itemList = item.split(":");
            log.info(JSON.toJSONString(itemList));
            ShellUtil.execShell(String.format("iptables -t nat  -D PREROUTING %s", itemList[0]));
            String snatStr = ShellUtil.execShell(String.format("iptables -L POSTROUTING -n -t nat --line-number|grep SNAT|grep %s|grep dpt:%s|grep %s|awk  '{print $1}'|sort -r|tr \"\\n\" \" \"", itemList[3], itemList[4], itemList[1]));
            if(StringUtils.isEmpty(snatStr)){
                continue;
            }
            snatStr = snatStr.trim();
            if(StringUtils.isEmpty(snatStr)){
                continue;
            }
            String[] snatList = snatStr.split(" ");
            log.info(JSON.toJSONString(snatList));
            for (String s : snatList) {
                if(StringUtils.isEmpty(s)){
                    continue;
                }
                ShellUtil.execShell("iptables -t nat  -D POSTROUTING "+ s);
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
