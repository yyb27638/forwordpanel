package com.leeroy.forwordpanel.forwordpanel.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.leeroy.forwordpanel.forwordpanel.model.Port;
import org.apache.ibatis.annotations.Update;

public interface PortDao extends BaseMapper<Port> {


    @Update("update port set disabled=#{disabled} where id=#{id}")
    void updateDisable(Boolean disabled, Integer id);
}
