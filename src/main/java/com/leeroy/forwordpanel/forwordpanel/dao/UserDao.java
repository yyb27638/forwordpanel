package com.leeroy.forwordpanel.forwordpanel.dao;

import com.leeroy.forwordpanel.forwordpanel.dto.AdminUserDTO;
import com.leeroy.forwordpanel.forwordpanel.dto.UserSearchDTO;
import com.leeroy.forwordpanel.forwordpanel.model.BaseAdminUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Mapper
public interface UserDao extends JpaRepository<BaseAdminUser, Integer> {
    @Select("<script>" +
            "select * from base_admin_user " +
            "<where>" +
            "<if test='sysUserName!=null'>" +
            " and sys_user_name=#{sysUserName}" +
            "</if>" +
            "<if test='userPhone!=null'>" +
            " and userPhone=#{sysUserName}" +
            "</if>" +
            "</where>" +
            "</script>")
    List<AdminUserDTO> getUserList(UserSearchDTO userSearch);

    @Select("<script>" +
            "select * from base_admin_user " +
            "where sys_user_name=#{sysUserName}" +
            "<if test='id!=null'>" +
            "and id<>#{id} " +
            "</if>" +
            "</script>")
    BaseAdminUser getUserByUserName(String sysUserName, Integer id);

    @Update("update base_admin_user set sys_user_pwd=#{password} where sys_user_name=#{username}")
    void updatePwd(String username, String password);

    @Update("update base_admin_user set user_status=#{userStatus} where id=#{id}")
    void updateUserStatus(Integer userStatus, Integer id);

    AdminUserDTO getAdminUserDTOBySysUserName(String username);
}
