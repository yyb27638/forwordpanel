package com.leeroy.forwordpanel.forwordpanel.controller;

import com.leeroy.forwordpanel.forwordpanel.common.WebCurrentData;
import com.leeroy.forwordpanel.forwordpanel.common.response.ApiResponse;
import com.leeroy.forwordpanel.forwordpanel.model.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 资源管理
 */
@RestController
public class ResourceController {

    private static Map<Integer, List<Resource>> USER_TYPE_RESOURCE = new HashMap<>();

    /**
     * 初始化权限
     */
    static {
        List<Resource> adminResourceList = new ArrayList<>();
        Resource systemManage = new Resource(1, "系统管理", "");
        List<Resource> systemManageChildList = new ArrayList<>();
        systemManageChildList.add(new Resource(3, "服务器管理", "/server/manage"));
        systemManageChildList.add(new Resource(3, "端口管理", "/port/manage"));
        systemManageChildList.add(new Resource(1, "账号管理", "/user/userManage"));
        systemManageChildList.add(new Resource(2, "中转管理", "/portForwardManage"));
        systemManageChildList.add(new Resource(3, "Clash配置", "/clashManage"));

        systemManage.setChildrens(systemManageChildList);
        adminResourceList.add(systemManage);
        USER_TYPE_RESOURCE.put(0, adminResourceList);

        List<Resource> userResourceList = new ArrayList<>();
        Resource userSystemManage = new Resource(1, "系统管理", "");
        List<Resource> userSystemManageChildList = new ArrayList<>();
        userSystemManageChildList.add(new Resource(2, "中转管理", "/portForwardManage"));
        userSystemManageChildList.add(new Resource(3, "Clash配置", "/clashManage"));
        userSystemManage.setChildrens(userSystemManageChildList);
        userResourceList.add(userSystemManage);
        USER_TYPE_RESOURCE.put(1, userResourceList);
    }

    /**
     * 获取资源
     *
     * @return
     */
    @GetMapping("/getResource")
    public ApiResponse getResource() {
        Integer userType = WebCurrentData.getUser().getUserType();
        return ApiResponse.ok(USER_TYPE_RESOURCE.get(userType));
    }
}
