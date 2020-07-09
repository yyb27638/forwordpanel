package com.leeroy.forwordpanel.forwordpanel.common;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class RestResult {

    private Boolean success;

    private String message;

    public static RestResult success() {
        return new RestResult(true, "");
    }

    public static RestResult success(String data) {
        return new RestResult(true, data);
    }
}
