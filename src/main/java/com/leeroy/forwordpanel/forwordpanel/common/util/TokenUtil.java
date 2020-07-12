package com.leeroy.forwordpanel.forwordpanel.common.util;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Slf4j
public class TokenUtil {

    private TokenUtil() {
    }

    private static final String TOKEN_KEY = "123456abc";

    /**
     * @param userName 用户名
     * @param password 密码
     * @return
     */
    public static String makeToken(String userName, String password) {
        Long now = System.currentTimeMillis();
        return DigestUtils.Md5(userName, password, now);
    }


    /**
     * get token from request
     *
     * @param request
     * @return
     */
    public static String getToken(HttpServletRequest request) {
        return getTokenFromCookies(request);
    }

    private static String getTokenFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if(cookies==null||cookies.length==0){
            return null;
        }
        for (Cookie cookie : cookies) {
            if ("token".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }


}
