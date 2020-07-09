package com.leeroy.forwordpanel.forwordpanel.controller;

import com.leeroy.forwordpanel.forwordpanel.common.RestResult;
import com.leeroy.forwordpanel.forwordpanel.model.PortForward;
import com.leeroy.forwordpanel.forwordpanel.service.ForwardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("forward")
@RestController
public class ForwardController {

    @Autowired
    private ForwardService forwardService;

    @PostMapping("add")
    public RestResult add(PortForward portForward) {
        forwardService.addForward(portForward.getRemoteHostAddr(), portForward.getRemoteHostPort(), portForward.getLocalPort());
        return RestResult.success();
    }


    @PostMapping("getFlow")
    public RestResult getFlow(PortForward portForward) {
        return RestResult.success(forwardService.getPortFlow(portForward.getRemoteHostAddr(), portForward.getRemoteHostPort()));
    }

    @PostMapping("resetFlow")
    public RestResult resetFlow(PortForward portForward) {
        forwardService.resetFlowCount(portForward.getRemoteHostAddr(), portForward.getRemoteHostPort());
        return RestResult.success();
    }

    @GetMapping("stop")
    public RestResult stop(PortForward portForward) {
        forwardService.stopForward(portForward.getRemoteHostAddr(), portForward.getRemoteHostPort(), portForward.getLocalPort());
        return RestResult.success();
    }
}
