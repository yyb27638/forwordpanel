package com.leeroy.forwordpanel.forwordpanel.service;

import com.leeroy.forwordpanel.forwordpanel.common.response.ApiResponse;
import com.leeroy.forwordpanel.forwordpanel.common.util.DigestUtils;
import com.leeroy.forwordpanel.forwordpanel.common.util.TokenUtil;
import com.leeroy.forwordpanel.forwordpanel.dto.LoginDTO;
import com.leeroy.forwordpanel.forwordpanel.model.User;
import com.leeroy.forwordpanel.forwordpanel.model.UserToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

@Service
public class LoginService {

    @Autowired
    private UserService userService;

    @Autowired
    private UserTokenService userTokenService;

    /**
     * 登录
     *
     * @param loginDTO
     * @return
     */
    public ApiResponse login(LoginDTO loginDTO) {
        if (StringUtils.isEmpty(loginDTO.getUsername()) || StringUtils.isEmpty(loginDTO.getPassword())) {
            return ApiResponse.error("400", "用户名密码不能为空");
        }
        User baseAdminUser = userService.findByUserName(loginDTO.getUsername());
        if (baseAdminUser == null) {
            return ApiResponse.error("403", "用户名密码错误");
        }
        if(baseAdminUser.getDisabled()){
            return ApiResponse.error("403", "账号已被禁用");
        }
        if (!DigestUtils.Md5(loginDTO.getUsername(), loginDTO.getPassword()).equals(baseAdminUser.getPassword())) {
            return ApiResponse.error("403", "用户名密码错误");
        }
        String token = TokenUtil.makeToken(baseAdminUser.getUsername(), baseAdminUser.getPassword());
        UserToken userToken = UserToken.getInstance(token, baseAdminUser.getUsername(), baseAdminUser.getId());
        userTokenService.saveUserToken(userToken);
        return ApiResponse.ok(userToken);
    }

    /**
     * 登出
     *
     * @return
     */
    public void logout(HttpServletRequest request) {
        String token = TokenUtil.getToken(request);
        userTokenService.delToken(token);
    }

}
