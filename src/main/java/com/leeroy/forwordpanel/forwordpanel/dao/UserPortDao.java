package com.leeroy.forwordpanel.forwordpanel.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.leeroy.forwordpanel.forwordpanel.model.UserPort;
import org.apache.ibatis.annotations.Update;

public interface UserPortDao extends BaseMapper<UserPort> {


    @Update("update user_port set disabled=#{disabled} where id=#{id}")
    void updateDisable(Boolean disabled, Integer id);
}
