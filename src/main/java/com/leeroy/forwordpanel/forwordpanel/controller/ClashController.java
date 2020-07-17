package com.leeroy.forwordpanel.forwordpanel.controller;

import com.leeroy.forwordpanel.forwordpanel.common.response.ApiResponse;
import com.leeroy.forwordpanel.forwordpanel.model.Clash;
import com.leeroy.forwordpanel.forwordpanel.service.ClashService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@Controller
public class ClashController {

    @Autowired
    private ClashService clashService;

    /**
     * 中转管理
     *
     * @return
     */
    @RequestMapping("/clashManage")
    public String userManage() {
        return "clash/index";
    }


    @ResponseBody
    @GetMapping("getClashList")
    public ApiResponse getUserPortList(Integer userId) {
        return ApiResponse.ok(clashService.findClashList());
    }

    @ResponseBody
    @PostMapping("saveClash")
    public ApiResponse saveUserPort(@RequestBody Clash clash) {
        return clashService.save(clash);
    }

    @ResponseBody
    @PostMapping("deleteClash")
    public ApiResponse delete(String id) {
        clashService.delClash(id);
        return ApiResponse.ok();
    }


    @ResponseBody
    @GetMapping("clash/{id}")
    public void getClashFileById(@PathVariable String id, HttpServletResponse response) {
        Clash clash = clashService.findClashById(id);
        try {
            String text = clash.getText();
            byte[] bytes = text.getBytes("UTF-8");
            response.setContentType("application/octet-stream;charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + id + ".yml\"");
            response.addHeader("Content-Length", "" + bytes.length);
            OutputStream stream = null;
            stream = response.getOutputStream();
            stream.write(bytes);
            stream.flush();
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
