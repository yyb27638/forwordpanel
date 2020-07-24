package com.leeroy.forwordpanel.forwordpanel.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.leeroy.forwordpanel.forwordpanel.common.response.ApiResponse;
import com.leeroy.forwordpanel.forwordpanel.dao.PortDao;
import com.leeroy.forwordpanel.forwordpanel.dao.ServerDao;
import com.leeroy.forwordpanel.forwordpanel.dao.UserPortDao;
import com.leeroy.forwordpanel.forwordpanel.model.Server;
import com.leeroy.forwordpanel.forwordpanel.model.UserPort;
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


    /**
     * 保存clash
     */
    public ApiResponse save(Server server) {
        if (StringUtils.isEmpty(server.getId())) {
            server.setCreateTime(new Date());
            server.setDeleted(false);
            server.setKey(UUID.randomUUID().toString());
            serverDao.insert(server);
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
        return serverDao.selectList(queryWrapper);
    }


    /**
     * 删除clash
     */
    public void delete(Integer id) {
        Server userPort = new Server();
        userPort.setId(id);
        userPort.setDeleted(true);
        serverDao.updateById(userPort);
    }

}
