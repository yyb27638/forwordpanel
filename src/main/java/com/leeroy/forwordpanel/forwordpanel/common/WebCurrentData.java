package com.leeroy.forwordpanel.forwordpanel.common;

import com.leeroy.forwordpanel.forwordpanel.model.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Builder
@Getter
@Setter
public class WebCurrentData implements Serializable {

    private User user;
    private Integer userId;
    private String userName;
    private String ip;

    public static User getUser() {
        return WebThreadLocalManager.get().user;
    }


    public static WebCurrentData setUser(User user) {
        WebCurrentData currentData = WebThreadLocalManager.get();
        currentData.user = user;
        setUserId(user.getId());
        setUserName(user.getUsername());
        return currentData;
    }


    public static String getUserName() {
        return WebThreadLocalManager.get().userName;
    }

    public static WebCurrentData setUserName(String userName) {
        WebCurrentData currentData = WebThreadLocalManager.get();
        currentData.userName = userName;
        return currentData;
    }

    public static Integer getUserId() {
        return WebThreadLocalManager.get().userId;
    }

    public static WebCurrentData setUserId(Integer userId) {
        WebCurrentData currentData = WebThreadLocalManager.get();
        currentData.userId = userId;
        return currentData;
    }

    public static String getIp() {
        return WebThreadLocalManager.get().ip;
    }

    private static WebCurrentData setIp(String ip) {
        WebCurrentData currentData = WebThreadLocalManager.get();
        currentData.ip = ip;
        return currentData;
    }
}
