package com.leeroy.forwordpanel.forwordpanel.controller;

import com.leeroy.forwordpanel.forwordpanel.common.response.ApiResponse;
import com.leeroy.forwordpanel.forwordpanel.model.Port;
import com.leeroy.forwordpanel.forwordpanel.service.PortService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequestMapping("port")
@Controller
public class PortController {

    @Autowired
    private PortService portService;

    /**
     * 页面
     *
     * @return
     */
    @RequestMapping("/manage")
    public String portManage() {
        return "port/index";
    }


    @ResponseBody
    @GetMapping("getList")
    public ApiResponse getUserPortList(Integer userId) {
        return ApiResponse.ok(portService.findList());
    }

    @ResponseBody
    @GetMapping("getFreePortList")
    public ApiResponse getFreePortList(Integer userId, Integer serverId) {
        return ApiResponse.ok(portService.findFreePortList(serverId));
    }


    @ResponseBody
    @RequestMapping(value = "save", method = RequestMethod.POST)
    public ApiResponse saveUserPort(@RequestBody Port port) {
        return portService.save(port);
    }

    @ResponseBody
    @PostMapping("delete")
    public ApiResponse delete(Integer id) {
        portService.delete(id);
        return ApiResponse.ok();
    }


}
