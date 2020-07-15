package com.leeroy.forwordpanel.forwordpanel.controller;

import com.leeroy.forwordpanel.forwordpanel.common.WebCurrentData;
import com.leeroy.forwordpanel.forwordpanel.common.response.ApiResponse;
import com.leeroy.forwordpanel.forwordpanel.model.Clash;
import com.leeroy.forwordpanel.forwordpanel.model.UserPort;
import com.leeroy.forwordpanel.forwordpanel.model.UserPortForward;
import com.leeroy.forwordpanel.forwordpanel.service.ClashService;
import com.leeroy.forwordpanel.forwordpanel.service.UserPortForwardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.Date;

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

    /**
     * 中转列表
     *
     * @return
     */
    @ResponseBody
    @GetMapping("getPortForwardList")
    public ApiResponse getList() {
        return ApiResponse.ok(clashService.findClashList());
    }


    @GetMapping("getClashList")
    public ApiResponse getUserPortList(Integer userId) {
        return ApiResponse.ok(clashService.findClashList());
    }

    @PostMapping("save")
    public ApiResponse saveUserPort(@RequestBody Clash clash) {
        return clashService.save(clash);
    }

    @GetMapping("delete")
    public ApiResponse delete(String id) {
        clashService.delClash(id);
        return ApiResponse.ok();
    }


    @GetMapping("clash/{id}")
    public void getClashFileById(@PathVariable String id, HttpServletResponse response) {
        Clash clash = clashService.findClashById(id);
        String text = clash.getText();
        byte[] bytes = text.getBytes();
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + id + ".yml\"");
        response.addHeader("Content-Length", "" + bytes.length);
        OutputStream stream = null;
        try {
            stream = response.getOutputStream();
            stream.write(bytes);
            stream.flush();
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
