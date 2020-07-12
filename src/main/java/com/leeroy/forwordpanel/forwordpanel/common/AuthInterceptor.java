package com.leeroy.forwordpanel.forwordpanel.common;

import com.alibaba.fastjson.JSON;
import com.leeroy.forwordpanel.forwordpanel.common.response.ApiResponse;
import com.leeroy.forwordpanel.forwordpanel.common.util.CertUtil;
import com.leeroy.forwordpanel.forwordpanel.common.util.TokenUtil;
import com.leeroy.forwordpanel.forwordpanel.dao.UserDao;
import com.leeroy.forwordpanel.forwordpanel.model.User;
import com.leeroy.forwordpanel.forwordpanel.model.UserToken;
import com.leeroy.forwordpanel.forwordpanel.service.UserTokenService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;

@Component
@Slf4j
public class AuthInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private UserTokenService userTokenService;

    @Autowired
    private UserDao userDao;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info(">>>>>>>>>>>>>>>登录验证 {}", request.getRequestURI());
        if (CertUtil.checkNoLogin(handler)) {
            return true;
        }
        log.info(">>>>>>>>>>>>>>>权限验证 {}", request.getRequestURI());
        // 验证权限
        if (CertUtil.checkNoAuth(handler)) {
            return true;
        }
        String token = TokenUtil.getToken(request);
        if (StringUtils.isEmpty(token)) {
            response.sendRedirect("/login");
            return false;
            //todo 跳转到登录
        }
        //根据token获取用户信息
        UserToken userToken = userTokenService.getUserByToken(token);
        if (Objects.isNull(userToken) || userToken.getUserId() == null) {
            //todo 跳转到登录页面
            response.sendRedirect("/login");
            return false;
        }
        //当前用户
        User user = userDao.selectById(userToken.getUserId());
        WebCurrentData.setUser(user);
        WebCurrentData.setUserId(user.getId());
        WebCurrentData.setUserName(user.getUsername());
        return true;
    }


    /**
     * ajax响应
     *
     * @param response
     * @param code
     * @param msg
     */
    private void renderResponse(HttpServletResponse response, String code, String msg) {
        ApiResponse baseResponse = ApiResponse.error(code, msg);
        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        PrintWriter pWriter = null;
        try {
            pWriter = response.getWriter();
            pWriter.write(JSON.toJSONString(baseResponse));
            pWriter.flush();
        } catch (IOException e) {
            e.getMessage();
        } finally {
            pWriter.close();
        }
    }

}
