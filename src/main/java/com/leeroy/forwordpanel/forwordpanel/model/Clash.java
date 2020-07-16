package com.leeroy.forwordpanel.forwordpanel.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * clash配置
 */
@Data
public class Clash {


    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    private String configName;
    private Integer userId;
    private Boolean disabled;
    private Boolean deleted;
    /**
     * 到期时间
     */
    private Long expireTime;
    private Long createTime;
    private Long updateTime;
    /**
     * 配置字符串
     */
    private String text;
}
