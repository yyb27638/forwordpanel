package com.leeroy.forwordpanel.forwordpanel.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 用户token
 */
@Data
@NoArgsConstructor
public class UserToken implements Serializable {


    /**
     * 用户token
     */
    private String token;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 超时时间
     */
    private Long expireTime;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 更新时间
     */
    private Long updateTime;

    public static UserToken getInstance(String token, String username, Integer userId) {
        UserToken userToken = new UserToken();
        userToken.setUsername(username);
        userToken.setUserId(userId);
        userToken.setToken(token);
        userToken.setExpireTime(System.currentTimeMillis()+1000*60*30);
        return userToken;
    }
}
