package com.device.manage.entity;

import lombok.Data;

import java.util.Date;

@Data
public class ProjectMenu {
    private Long id;
    private Long projectId;
    private Long menuId;
    private Date createTime;
    private Date updateTime;
}