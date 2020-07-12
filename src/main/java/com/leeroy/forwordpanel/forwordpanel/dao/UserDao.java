package com.leeroy.forwordpanel.forwordpanel.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.leeroy.forwordpanel.forwordpanel.dto.UserSearchDTO;
import com.leeroy.forwordpanel.forwordpanel.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface UserDao extends BaseMapper<User> {


    @Select("<script>" +
            "select *  from sys_user " +
            "<where>" +
            " and deleted = false"+
            "<if test='username!=null and username!=\"\"'>" +
            " and username=#{username}" +
            "</if>" +
            "<if test='userPhone!=null  and userPhone!=\"\"'>" +
            " and user_phone=#{userPhone}" +
            "</if>" +
            "<if test='telegram!=null  and telegram!=\"\"'>" +
            " and telegram=#{telegram}" +
            "</if>" +
            "<if test='disabled!=null'>" +
            " and disabled=#{disabled}" +
            "</if>" +
            "<if test='id!=null'>" +
            " and id=#{id}" +
            "</if>" +
            "</where>" +
            "</script>")
    List<User> getUsers(UserSearchDTO userSearch);

    @Select("<script>" +
            "select * from sys_user " +
            "where username=#{username} and deleted = false " +
            "<if test='id!=null'>" +
            "and <![CDATA[ id<>#{id} ]]> " +
            "</if>" +
            "</script>")
    User getUserByUserName(String username, Integer id);

    @Update("update sys_user set password=#{password} where username=#{username}")
    void updatePwd(String username, String password);

    @Update("update sys_user set disabled=#{disabled} where id=#{id}")
    void updateDisable(Boolean disabled, Integer id);

    @Update("update sys_user set deleted=true where id=#{id}")
    void deleteUser(Integer id);
}
