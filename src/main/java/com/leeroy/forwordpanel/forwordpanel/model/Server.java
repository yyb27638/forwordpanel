package com.leeroy.forwordpanel.forwordpanel.model;

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
public class Server {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 服务器名称
     */
    private String serverName;

    /**
     * 服务器host
     */
    private String host;

    /**
     * ssh端口
     */
    private Integer port;

    /**
     * 服务器 key
     */
    private String key;

    /**
     * 服务器状态
     */
    private String state;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date updateTime;

    private Boolean deleted;
}
