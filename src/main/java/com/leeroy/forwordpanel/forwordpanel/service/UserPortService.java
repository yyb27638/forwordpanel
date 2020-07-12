package com.leeroy.forwordpanel.forwordpanel.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.leeroy.forwordpanel.forwordpanel.common.WebCurrentData;
import com.leeroy.forwordpanel.forwordpanel.common.response.ApiResponse;
import com.leeroy.forwordpanel.forwordpanel.dao.UserPortDao;
import com.leeroy.forwordpanel.forwordpanel.model.UserPort;
import com.leeroy.forwordpanel.forwordpanel.model.UserPortForward;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserPortService {

    @Autowired
    private UserPortDao userPortDao;

    @Autowired
    private UserPortForwardService userPortForwardService;

    /**
     * 给用户分配端口
     * @param userPort
     */
    public void save(UserPort userPort) {
        if (userPort.getId() == null) {
            if (findByUserIdAndPort(userPort.getUserId(), userPort.getLocalPort()) != null) {
                return;
            }
            userPort.setDeleted(false);
            userPort.setDisabled(false);
            userPort.setCreateTime(System.currentTimeMillis());
            userPortDao.insert(userPort);
        } else {
            userPort.setUpdateTime(System.currentTimeMillis());
            userPortDao.updateById(userPort);
        }
        //创建中转记录
        userPortForwardService.createUserPortForward(userPort.getLocalPort(), userPort.getUserId());
    }


    /**
     * 查询用户端口
     * @param userId
     * @return
     */
    public List<UserPort> findUserPortList(Integer userId) {
        LambdaQueryWrapper<UserPort> queryWrapper = Wrappers.<UserPort>lambdaQuery().eq(UserPort::getUserId, userId)
                .eq(UserPort::getDeleted, false);
        return userPortDao.selectList(queryWrapper);
    }

    /**
     * 删除用户端口
     */
    public void delUserPort(Integer id){
        UserPort existPort = userPortDao.selectById(id);
        UserPort userPort = new UserPort();
        userPort.setId(id);
        userPort.setDeleted(true);
        userPortDao.updateById(userPort);
        //删除中转记录
        userPortForwardService.deleteUserPortForward(existPort.getLocalPort(), existPort.getUserId());
    }

    /**
     * 查询用户端口
     * @param userId
     * @param port
     * @return
     */
    public UserPort findByUserIdAndPort(Integer userId, Integer port) {
        LambdaQueryWrapper<UserPort> queryWrapper = Wrappers.<UserPort>lambdaQuery().eq(UserPort::getLocalPort, port)
                .eq(UserPort::getDeleted, false);
        return userPortDao.selectOne(queryWrapper);
    }

    /**
     * 禁用用户
     *
     * @param id
     * @return
     */
    public ApiResponse disablePort(Integer id) {
        if (id.equals(WebCurrentData.getUserId())) {
            return ApiResponse.error("400", "您不能禁用自己");
        }
        UserPort existPort = userPortDao.selectById(id);
        UserPortForward userPortForward = new UserPortForward();
        userPortForward.setLocalPort(existPort.getLocalPort());
        userPortForward.setUserId(existPort.getUserId());
        userPortForwardService.stopForward(userPortForward);
        userPortDao.updateDisable(true, id);
        return ApiResponse.ok();
    }

    /**
     * 启用用户
     *
     * @param id
     * @return
     */
    public ApiResponse enablePort(Integer id) {
        if (id.equals(WebCurrentData.getUserId())) {
            return ApiResponse.error("400", "您不能启用自己");
        }
        userPortDao.updateDisable(false, id);
        return ApiResponse.ok();
    }

}
