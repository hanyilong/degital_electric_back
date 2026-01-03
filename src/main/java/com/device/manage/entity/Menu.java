package com.device.manage.entity;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class Menu {
    private Long id;
    private String name;
    private String key;
    private String path;
    private String icon;
    private String component;
    private Long parentId;
    private Integer sortOrder;
    private Integer status;
    private Date createTime;
    private Date updateTime;
    private List<Menu> children;
}