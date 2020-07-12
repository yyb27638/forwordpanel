package com.leeroy.forwordpanel.forwordpanel.controller;

import com.leeroy.forwordpanel.forwordpanel.common.WebCurrentData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Title: LoginController
 * @Description:
 * @author: youqing
 * @version: 1.0
 * @date: 2018/11/20 11:39
 */
@Controller
public class IndexController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @RequestMapping("login")
    public String tologin(){
        logger.info("定向登陆页");
        return "login";
    }

    @RequestMapping("/")
    public String index(){
        logger.info("定向主页");

        return "home";
    }

    @RequestMapping("home")
    public String home(Model model){
        logger.info("定向主页");
        model.addAttribute("username", WebCurrentData.getUserName());
        return "home";
    }

}
