package com.leeroy.forwordpanel.forwordpanel.model;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * 端口
 */
@Data
public class UserPort {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    private Integer userId;
    private Integer localPort;
    private Boolean disabled;
    private Boolean deleted;
    /**
     * 端口流量限制
     */
    private Long dataLimit;
    /**
     * 到期时间
     */
    private Long expireTime;
    private Long createTime;
    private Long updateTime;
}
