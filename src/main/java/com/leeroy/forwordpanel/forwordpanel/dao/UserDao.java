package com.leeroy.forwordpanel.forwordpanel.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.leeroy.forwordpanel.forwordpanel.dto.AdminUserDTO;
import com.leeroy.forwordpanel.forwordpanel.dto.UserSearchDTO;
import com.leeroy.forwordpanel.forwordpanel.model.BaseAdminUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface UserDao extends BaseMapper<BaseAdminUser> {


    @Select("<script>" +
            "select id, sys_user_name as sysUserName, sys_user_pwd as sysUserPwd,role_id as roleId, user_phone as userPhone, reg_time as regTime, user_status as userStatus from base_admin_user " +
            "<where>" +
            "<if test='sysUserName!=null'>" +
            " and sys_user_name=#{sysUserName}" +
            "</if>" +
            "<if test='userPhone!=null'>" +
            " and userPhone=#{userPhone}" +
            "</if>" +
            "</where>" +
            "</script>")
    List<BaseAdminUser> getUsers(UserSearchDTO userSearch);

    @Select("<script>" +
            "select * from base_admin_user " +
            "where sys_user_name=#{sysUserName} " +
            "<if test='id!=null'>" +
            "and <![CDATA[ id<>#{id} ]]> " +
            "</if>" +
            "</script>")
    BaseAdminUser getUserByUserName(String sysUserName, Integer id);

    @Update("update base_admin_user set sys_user_pwd=#{password} where sys_user_name=#{username}")
    void updatePwd(String username, String password);

    @Update("update base_admin_user set user_status=#{userStatus} where id=#{id}")
    void updateStatus(Integer userStatus, Integer id);

    AdminUserDTO getAdminUserDTOBySysUserName(String username);
}
