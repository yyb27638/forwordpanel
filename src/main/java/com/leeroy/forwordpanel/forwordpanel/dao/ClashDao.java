package com.leeroy.forwordpanel.forwordpanel.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.leeroy.forwordpanel.forwordpanel.model.Clash;
import com.leeroy.forwordpanel.forwordpanel.model.UserPort;
import org.apache.ibatis.annotations.Update;

public interface ClashDao extends BaseMapper<Clash> {


    @Update("update clash set disabled=#{disabled} where id=#{id}")
    void updateDisable(Boolean disabled, Integer id);
}
