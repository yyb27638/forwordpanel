package com.leeroy.forwordpanel.forwordpanel.dto;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 端口
 */
@Data
public class UserPortDTO {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    private Integer userId;
    private Integer portId;
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
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date expireTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date updateTime;

    /**
     * 所属服务器
     */
    private Integer serverId;

    /**
     * 服务器名称
     */
    private String serverName;

    /**
     * 服务起ip
     */
    private String serverHost;
}
