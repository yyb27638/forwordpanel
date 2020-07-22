package com.leeroy.forwordpanel.forwordpanel.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 端口转发实体
 */
@Data
public class UserPortForward {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    // 中转用户
    private Integer userId;
    //端口id
    private Integer portId;
    // 目标ip
    private String remoteIp;
    // 目标主机地址
    private String remoteHost;
    // 流量使用量
    private Long dataUsage;
    // 目标主机端口
    private Integer remotePort;
    // 创建时间
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createTime;
    //更新时间
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date updateTime;
    //删除表示
    private Boolean deleted;
    //禁用表示
    private Boolean disabled;
}
