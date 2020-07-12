package com.leeroy.forwordpanel.forwordpanel.common.response;

import com.leeroy.forwordpanel.forwordpanel.common.IStatusMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: meishaoyong
 * @Date: 2019/11/8 13:41
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {

    private String code = "0";

    private String msg = "";

    private T data;

    private Boolean success;

    public static ApiResponse ok() {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setSuccess(true);
        return apiResponse;
    }

    public static <T> ApiResponse<T> ok(T data) {
        ApiResponse result = ok();
        result.setData(data);
        return result;
    }

    public static <T> ApiResponse<T> ok(String code, String msg, T data) {
        ApiResponse result = ok();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }

    public static ApiResponse ok(IStatusMessage.SystemStatus systemStatus) {
        ApiResponse result = new ApiResponse();
        result.setCode(systemStatus.getCode());
        result.setMsg(systemStatus.getMessage());
        result.setSuccess(true);
        return result;
    }

    public static ApiResponse error(String code, String msg) {
        ApiResponse result = new ApiResponse();
        result.setCode(code);
        result.setMsg(msg);
        result.setSuccess(false);
        return result;
    }

    public static ApiResponse error(IStatusMessage.SystemStatus systemStatus) {
        ApiResponse result = new ApiResponse();
        result.setCode(systemStatus.getCode());
        result.setMsg(systemStatus.getMessage());
        result.setSuccess(false);
        return result;
    }
}
