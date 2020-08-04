package com.leeroy.forwordpanel.forwordpanel.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.leeroy.forwordpanel.forwordpanel.common.WebCurrentData;
import com.leeroy.forwordpanel.forwordpanel.common.response.ApiResponse;
import com.leeroy.forwordpanel.forwordpanel.dao.ServerDao;
import com.leeroy.forwordpanel.forwordpanel.dao.UserServerDao;
import com.leeroy.forwordpanel.forwordpanel.model.Server;
import com.leeroy.forwordpanel.forwordpanel.model.UserServer;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ServerService {

    @Autowired
    private ServerDao serverDao;

    @Autowired
    private UserServerDao userServerDao;


    /**
     * 保存clash
     */
    public ApiResponse save(Server server) {
        if (StringUtils.isEmpty(server.getId())) {
            server.setCreateTime(new Date());
            server.setDeleted(false);
            server.setKey(UUID.randomUUID().toString());
            server.setOwnerId(WebCurrentData.getUserId());
            serverDao.insert(server);
            UserServer userServer = new UserServer();
            userServer.setUserId(WebCurrentData.getUserId());
            userServer.setServerId(server.getId());
            userServer.setDeleted(false);
            userServerDao.insert(userServer);
        } else {
            Server existPort = serverDao.selectById(server.getId());
            BeanUtils.copyProperties(server, existPort);
            existPort.setUpdateTime(new Date());
            serverDao.updateById(existPort);
        }
        return ApiResponse.ok();
    }


    /**
     * 查询clash列表
     *
     * @return
     */
    public List<Server> findList() {
        LambdaQueryWrapper<Server> queryWrapper = Wrappers.<Server>lambdaQuery().eq(Server::getDeleted, false);
        List<Server> serverList = serverDao.selectList(queryWrapper);
        LambdaQueryWrapper<UserServer> userServerQueryWrapper = Wrappers.<UserServer>lambdaQuery().eq(UserServer::getDeleted, false);
        if (WebCurrentData.getUser().getUserType() > 0) {
            userServerQueryWrapper = userServerQueryWrapper.eq(UserServer::getUserId, WebCurrentData.getUserId());
        }
        List<UserServer> userServerList = userServerDao.selectList(userServerQueryWrapper);
        serverList = serverList.stream().filter(server -> {
            for (UserServer userServer : userServerList) {
                if (server.getId().equals(userServer.getServerId())) {
                    return true;
                }
            }
            return false;
        }).collect(Collectors.toList());
        serverList.stream().forEach(server -> {
            server.setPassword(null);
        });
        return serverList;
    }


    /**
     * 删除clash
     */
    public void delete(Integer id) {
        Server userPort = new Server();
        userPort.setId(id);
        userPort.setDeleted(true);
        serverDao.updateById(userPort);
        LambdaQueryWrapper<UserServer> userServerQueryWrapper = Wrappers.<UserServer>lambdaQuery().eq(UserServer::getDeleted, false).eq(UserServer::getServerId, id);
        userServerDao.delete(userServerQueryWrapper);
    }

}
