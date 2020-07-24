package com.leeroy.forwordpanel.forwordpanel.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.leeroy.forwordpanel.forwordpanel.model.Port;
import com.leeroy.forwordpanel.forwordpanel.model.Server;
import org.apache.ibatis.annotations.Update;

public interface ServerDao extends BaseMapper<Server> {


    @Update("update server set disabled=#{disabled} where id=#{id}")
    void updateDisable(Boolean disabled, Integer id);
}
