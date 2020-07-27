package com.leeroy.forwordpanel.forwordpanel.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 端口信息
 */
@Data
public class PortDTO {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

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

    /**
     * 本地端口
     */
    private Integer localPort;

    /**
     * 外网端口
     */
    private Integer internetPort;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date updateTime;

    private Boolean deleted;
}
