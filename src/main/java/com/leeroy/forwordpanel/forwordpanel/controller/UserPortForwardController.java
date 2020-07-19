package com.leeroy.forwordpanel.forwordpanel.controller;

import com.leeroy.forwordpanel.forwordpanel.common.WebCurrentData;
import com.leeroy.forwordpanel.forwordpanel.common.response.ApiResponse;
import com.leeroy.forwordpanel.forwordpanel.model.User;
import com.leeroy.forwordpanel.forwordpanel.model.UserPortForward;
import com.leeroy.forwordpanel.forwordpanel.service.UserPortForwardService;
import com.leeroy.forwordpanel.forwordpanel.service.UserService;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserPortForwardController {

    @Autowired
    private UserPortForwardService forwardService;

    @Autowired
    private UserService userService;

    /**
     * 中转管理
     *
     * @return
     */
    @RequestMapping("/portForwardManage")
    public String userManage(Model model) {
        User user = userService.getUserById(WebCurrentData.getUserId());
        model.addAttribute("expireTime", user.getExpireTime()==null?"": DateFormatUtils.format(user.getExpireTime(), "yyyy-MM-dd"));
        return "forward/forwardManage";
    }

    /**
     * 中转列表
     *
     * @return
     */
    @ResponseBody
    @GetMapping("getPortForwardList")
    public ApiResponse getList() {
        return forwardService.getUserForwardList();
    }

    /**
     * 开始中转
     *
     * @param userPortForward
     * @return
     */
    @ResponseBody
    @PostMapping("startForward")
    public ApiResponse startForward(UserPortForward userPortForward) {
        userPortForward.setUserId(WebCurrentData.getUserId());
        return forwardService.startForward(userPortForward);
    }

    /**
     * 停止中转
     *
     * @param userPortForward
     * @return
     */
    @ResponseBody
    @PostMapping("stopForward")
    public ApiResponse stopFroward(UserPortForward userPortForward) {
        userPortForward.setUserId(WebCurrentData.getUserId());
        return forwardService.stopForward(userPortForward);
    }

}
