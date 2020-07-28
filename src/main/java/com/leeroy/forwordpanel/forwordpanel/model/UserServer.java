package com.leeroy.forwordpanel.forwordpanel.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class UserServer {

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    /**
     * 用户id
     */
    private Integer userId;
    /**
     * 服务器id
     */
    private Integer serverId;

    private Boolean deleted;
}
