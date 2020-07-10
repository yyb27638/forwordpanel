package com.leeroy.forwordpanel.forwordpanel.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


@TableName("base_admin_user")
@Data
public class BaseAdminUser {
    /**
     * ID
     */
    private Integer id;

    /**
     * 系统用户名称
     */
    @TableField("sys_user_name")
    private String sysUserName;

    /**
     * 系统用户密码
     */
    private String sysUserPwd;

    /**
     * 角色
     */
    private Integer roleId;

    /**
     * 手机号
     */
    private String userPhone;

    /**
     * 登记时间
     */
    private String regTime;

    /**
     * 状态（0：无效；1：有效）
     */
    private Integer userStatus;


}
