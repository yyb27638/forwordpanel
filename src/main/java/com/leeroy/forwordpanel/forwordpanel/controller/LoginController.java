package com.leeroy.forwordpanel.forwordpanel.controller;

import com.leeroy.forwordpanel.forwordpanel.common.annotation.NoAuth;
import com.leeroy.forwordpanel.forwordpanel.common.annotation.NoLogin;
import com.leeroy.forwordpanel.forwordpanel.common.response.ApiResponse;
import com.leeroy.forwordpanel.forwordpanel.common.util.TokenUtil;
import com.leeroy.forwordpanel.forwordpanel.dto.LoginDTO;
import com.leeroy.forwordpanel.forwordpanel.service.LoginService;
import com.leeroy.forwordpanel.forwordpanel.service.UserTokenService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Api(value = "登录")
@RestController
@Validated
public class LoginController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private UserTokenService userTokenService;

    @NoLogin
    @NoAuth
    @PostMapping("/login")
    @ResponseBody
    public ApiResponse login(LoginDTO loginDTO) {
        return loginService.login(loginDTO);
    }

    @RequestMapping("/logout")
    @NoLogin
    public ModelAndView logout(HttpServletRequest request) {
        String token = TokenUtil.getToken(request);
        userTokenService.delToken(token);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        return modelAndView;
    }

}
