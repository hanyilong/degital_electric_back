package com.device.manage.entity;

import java.io.Serializable;

public class CabinetRoom implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String name;
    private String rows;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRows() {
        return rows;
    }

    public void setRows(String rows) {
        this.rows = rows;
    }
}