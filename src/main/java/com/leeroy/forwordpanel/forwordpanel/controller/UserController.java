package com.leeroy.forwordpanel.forwordpanel.controller;


import com.leeroy.forwordpanel.forwordpanel.common.WebCurrentData;
import com.leeroy.forwordpanel.forwordpanel.common.response.ApiResponse;
import com.leeroy.forwordpanel.forwordpanel.common.response.PageDataResult;
import com.leeroy.forwordpanel.forwordpanel.dao.UserPortDao;
import com.leeroy.forwordpanel.forwordpanel.dto.UserSearchDTO;
import com.leeroy.forwordpanel.forwordpanel.model.User;
import com.leeroy.forwordpanel.forwordpanel.model.UserPort;
import com.leeroy.forwordpanel.forwordpanel.service.UserPortService;
import com.leeroy.forwordpanel.forwordpanel.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户管理
 */
@Controller
@RequestMapping("user")
public class UserController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserService userService;

    @Autowired
    private UserPortService userPortService;


    /**
     * 设置密码
     * @param pwd
     * @param isPwd
     * @return
     */
    @RequestMapping("setPwd")
    @ResponseBody
    public ApiResponse setPassword(String pwd, String isPwd) {
        logger.info("进行密码重置");
        Map<String, Object> data = new HashMap();
        if (!pwd.equals(isPwd)) {
            return ApiResponse.error("400", "两次密码不一致");
        }
        //获取当前登陆的用户信息
        User user = WebCurrentData.getUser();
        userService.updatePwd(user.getUsername(), pwd);
        return ApiResponse.ok();
    }

    /**
     * 功能描述: 跳到系统用户列表
     *
     * @param:
     * @return:
     * @auther: youqing
     * @date: 2018/11/21 13:50
     */
    @RequestMapping("/userManage")
    public String userManage() {
        return "user/userManage";
    }

    /**
     * 用户分页
     *
     * @param pageNum
     * @param pageSize
     * @param userSearch
     * @return
     */
    @RequestMapping(value = "/getUserList", method = RequestMethod.POST)
    @ResponseBody
    public PageDataResult getUserList(@RequestParam("pageNum") Integer pageNum, @RequestParam("pageSize") Integer pageSize, UserSearchDTO userSearch) {
        PageDataResult pdr = new PageDataResult();
        try {
            if (null == pageNum) {
                pageNum = 1;
            }
            if (null == pageSize) {
                pageSize = 10;
            }
            // 获取用户列表
            pdr = userService.getUserList(userSearch, pageNum, pageSize);
            logger.info("用户列表查询=pdr:" + pdr);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("用户列表查询异常！", e);
        }
        return pdr;
    }


    /**
     * 新增/更新用户
     *
     * @param user
     * @return
     */
    @RequestMapping(value = "/setUser", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponse setUser(User user) {
        logger.info("设置用户[新增或更新]！user:" + user);
        Map<String, Object> data = new HashMap();
        if (user.getId() == null) {
            return userService.addUser(user);
        } else {
            return userService.updateUser(user);
        }
    }


    /**
     * 禁用用户
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/disable", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponse disable(@RequestParam("id") Integer id) {
         return userService.disableUser(id);
    }

    /**
     * 启用用户
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/enable", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponse enable(@RequestParam("id") Integer id) {
        return userService.enableUser(id);
    }


    /**
     * 启用用户
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponse delete(@RequestParam("id") Integer id) {
        List<UserPort> userPortList = userPortService.findUserPortList(id);
        if(!CollectionUtils.isEmpty(userPortList)){
            return ApiResponse.error("401", "请先删除用户端口");
        }
        return userService.delUser(id);
    }
}
