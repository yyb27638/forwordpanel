package com.leeroy.forwordpanel.forwordpanel.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.leeroy.forwordpanel.forwordpanel.common.WebCurrentData;
import com.leeroy.forwordpanel.forwordpanel.common.response.ApiResponse;
import com.leeroy.forwordpanel.forwordpanel.dao.UserPortDao;
import com.leeroy.forwordpanel.forwordpanel.dao.UserPortForwardDao;
import com.leeroy.forwordpanel.forwordpanel.model.UserPort;
import com.leeroy.forwordpanel.forwordpanel.model.UserPortForward;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 用户中转
 */
@Slf4j
@Service
public class UserPortForwardService {

    @Autowired
    private UserPortForwardDao userPortForwardDao;

    @Autowired
    private UserPortDao userPortDao;

    @Autowired
    private ForwardService forwardService;

    /**
     * 查询用户中转
     *
     * @return
     */
    public ApiResponse<List<UserPortForward>> getUserForwardList() {
        Integer userId = WebCurrentData.getUserId();
        LambdaQueryWrapper<UserPortForward> queryWrapper;
        if(WebCurrentData.getUser().getUserType()==0){
            queryWrapper  = Wrappers.<UserPortForward>lambdaQuery()
                    .eq(UserPortForward::getDeleted, false);
        }else{
            queryWrapper = Wrappers.<UserPortForward>lambdaQuery().eq(UserPortForward::getUserId, userId)
                    .eq(UserPortForward::getDeleted, false);
        }
        List<UserPortForward> userPortForwardList = userPortForwardDao.selectList(queryWrapper);
        for (UserPortForward userPortForward : userPortForwardList) {
            String flow = forwardService.getPortFlow(userPortForward.getRemoteIp(), userPortForward.getRemotePort());
            userPortForward.setDataUsage(Long.valueOf(flow));
        }
        return ApiResponse.ok(userPortForwardList);
    }



    /**
     * 创建端口转发
     *
     * @param localPort
     * @param userId
     */
    public void createUserPortForward(Integer localPort, Integer userId) {
        LambdaQueryWrapper<UserPortForward> queryWrapper = Wrappers.<UserPortForward>lambdaQuery().eq(UserPortForward::getUserId, userId)
                .eq(UserPortForward::getLocalPort, localPort).eq(UserPortForward::getDeleted, false);
        UserPortForward exist = userPortForwardDao.selectOne(queryWrapper);
        if (exist == null) {
            UserPortForward userPortForward = new UserPortForward();
            userPortForward.setLocalPort(localPort);
            userPortForward.setUserId(userId);
            userPortForward.setDeleted(false);
            userPortForward.setCreateTime(new Date());
            userPortForward.setDisabled(true);
            userPortForwardDao.insert(userPortForward);
        }
    }

    /**
     * 删除中转
     *
     * @param localPort
     * @param userId
     */
    public void deleteUserPortForward(Integer localPort, Integer userId) {
        LambdaQueryWrapper<UserPortForward> queryWrapper = Wrappers.<UserPortForward>lambdaQuery().eq(UserPortForward::getUserId, userId)
                .eq(UserPortForward::getLocalPort, localPort).eq(UserPortForward::getDeleted, false).eq(UserPortForward::getDisabled, false);
        UserPortForward portForward = userPortForwardDao.selectOne(queryWrapper);
        if (portForward != null) {
            //停止中转
            forwardService.stopForward(portForward.getRemoteIp(), portForward.getRemotePort(), portForward.getLocalPort());
        }
        //删除中转记录
        queryWrapper = Wrappers.<UserPortForward>lambdaQuery().eq(UserPortForward::getUserId, userId)
                .eq(UserPortForward::getLocalPort, localPort);
        UserPortForward userPortForward = new UserPortForward();
        userPortForward.setDeleted(true);
        userPortForwardDao.update(userPortForward, queryWrapper);
    }

    /**
     * 开启中转
     *
     * @param userPortForward
     * @return
     */
    public ApiResponse startForward(UserPortForward userPortForward) {
        ApiResponse apiResponse = permissionCheck(userPortForward.getUserId());
        if (!apiResponse.getSuccess()) {
            return apiResponse;
        }
        Integer userId = WebCurrentData.getUserId();
        //检查用户是否拥有此端口
        LambdaQueryWrapper<UserPort> userPortQueryWrapper = Wrappers.<UserPort>lambdaQuery().eq(UserPort::getUserId, userId)
                .eq(UserPort::getDeleted, false);
        List<UserPort> existUserPortList = userPortDao.selectList(userPortQueryWrapper);
        Boolean hasPort = false;
        for (UserPort userPort : existUserPortList) {
            if (userPort.getLocalPort().equals(userPortForward.getLocalPort())) {
                if (userPort.getDisabled()) {
                    return ApiResponse.error("403", "端口已被管理员禁用,请联系管理员");
                }
                hasPort = true;
                break;
            }
        }
        if (!hasPort) {
            return ApiResponse.error("403", "用户没有此端口的权限");
        }
        if (StringUtils.isBlank(userPortForward.getRemoteHost()) || userPortForward.getRemotePort() == null) {
            return ApiResponse.error("401", "请填写域名(IP)|端口");
        }
        //查询该端口已经存在的转发
        LambdaQueryWrapper<UserPortForward> queryWrapper = Wrappers.<UserPortForward>lambdaQuery().eq(UserPortForward::getUserId, userPortForward.getUserId())
                .eq(UserPortForward::getLocalPort, userPortForward.getLocalPort()).eq(UserPortForward::getDeleted, false);
        UserPortForward portForward = userPortForwardDao.selectOne(queryWrapper);
        if (!portForward.getDisabled()) {
            //停止中转
            forwardService.stopForward(portForward.getRemoteIp(), portForward.getRemotePort(), portForward.getLocalPort());
        }
        userPortForward.setRemoteIp(getRemoteIp(userPortForward.getRemoteHost()));

        //开始新的中转
        forwardService.addForward(userPortForward.getRemoteIp(), userPortForward.getRemotePort(), userPortForward.getLocalPort());
        //更新中转信息
        portForward.setUpdateTime(new Date());
        portForward.setRemoteIp(userPortForward.getRemoteIp());
        portForward.setRemoteHost(userPortForward.getRemoteHost());
        portForward.setRemotePort(userPortForward.getRemotePort());
        portForward.setDisabled(false);
        userPortForwardDao.updateById(portForward);
        return ApiResponse.ok();
    }

    /**
     * 停用中转
     *
     * @return
     */
    public ApiResponse stopForward(UserPortForward userPortForward) {
        Integer userId = userPortForward.getUserId();
        Integer localPort = userPortForward.getLocalPort();
        ApiResponse apiResponse = permissionCheck(userId);
        if (!apiResponse.getSuccess()) {
            return apiResponse;
        }
        LambdaQueryWrapper<UserPortForward> queryWrapper = Wrappers.<UserPortForward>lambdaQuery().eq(UserPortForward::getUserId, userId)
                .eq(UserPortForward::getLocalPort, localPort).eq(UserPortForward::getDeleted, false);
        UserPortForward portForward = userPortForwardDao.selectOne(queryWrapper);
        if (!portForward.getDisabled()) {
            //停止中转
            forwardService.stopForward(portForward.getRemoteIp(), portForward.getRemotePort(), portForward.getLocalPort());
        }
        //停止中转记录
        userPortForwardDao.updateDisable(true, portForward.getId());
        return ApiResponse.ok();
    }

    /**
     * 越权检查
     *
     * @param userId
     * @return
     */
    private ApiResponse permissionCheck(Integer userId) {
        if (WebCurrentData.getUser().getUserType() > 0 && !WebCurrentData.getUserId().equals(userId)) {
            return ApiResponse.error("403", "您没有权限执行该操作");
        }
        return ApiResponse.ok();
    }

    public static void main(String[] args) throws UnknownHostException {
        System.out.println(getRemoteIp("hk.xiaolifly.xyz"));
    }

    /**
     * 获取域名ip
     *
     * @param remoteHost
     * @return
     * @throws Exception
     */
    public static String getRemoteIp(String remoteHost) {
        if (isboolIp(remoteHost)) {
            return remoteHost;
        }
        try {
            InetAddress addr = InetAddress.getByName(remoteHost);
            return addr.getHostAddress();
        } catch (UnknownHostException e) {
            log.error("解析域名错误", e);
        }
        return null;
    }

    /**
     * 判断是否为合法IP * @return the ip
     */
    public static boolean isboolIp(String ipAddress) {
        String ip = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
        Pattern pattern = Pattern.compile(ip);
        Matcher matcher = pattern.matcher(ipAddress);
        return matcher.matches();
    }


}
