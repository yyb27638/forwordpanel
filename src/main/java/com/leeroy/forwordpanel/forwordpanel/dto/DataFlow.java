package com.leeroy.forwordpanel.forwordpanel.dto;

import lombok.Data;

@Data
public class DataFlow {
    public DataFlow(String tcp, String udp) {
        this.tcp = tcp;
        this.udp = udp;
    }

    private String tcp;
    private String udp;
}
