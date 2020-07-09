package com.leeroy.forwordpanel.forwordpanel.model;

import lombok.Data;

/**
 * 端口转发实体
 */
@Data
public class PortForward {

    // 监听本地端口
    private int localPort;
    // 目标主机地址
    private String remoteHostAddr;
    // 目标主机端口
    private int remoteHostPort;
}
