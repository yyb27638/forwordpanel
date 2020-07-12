package com.leeroy.forwordpanel.forwordpanel.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.leeroy.forwordpanel.forwordpanel.model.UserPortForward;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserPortForwardDao extends BaseMapper<UserPortForward> {


    @Update("update user_port_forward set disabled=#{disabled} where id=#{id}")
    void updateDisable(Boolean disabled, Integer id);

}
