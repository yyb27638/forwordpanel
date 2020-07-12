package com.leeroy.forwordpanel.forwordpanel.model;

import lombok.Data;

import java.util.List;

/**
 * 资源
 */
@Data
public class Resource {

    private Integer id;

    private String name;

    private String url;

    private List<Resource> childrens;

    public Resource(Integer id, String name, String url) {
        this.id = id;
        this.name = name;
        this.url = url;
    }
}
