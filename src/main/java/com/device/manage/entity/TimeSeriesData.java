package com.device.manage.entity;

import java.util.Date;

public class TimeSeriesData {
    private Long id;
    private String deviceCode;
    private String pointName;
    private Double pointValue;
    private Date recordTime;
    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    public String getPointName() {
        return pointName;
    }

    public void setPointName(String pointName) {
        this.pointName = pointName;
    }

    public Double getPointValue() {
        return pointValue;
    }

    public void setPointValue(Double pointValue) {
        this.pointValue = pointValue;
    }

    public Date getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(Date recordTime) {
        this.recordTime = recordTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "TimeSeriesData{" +
                "  id=" + id + "," +
                "  deviceCode='" + deviceCode + "'," +
                "  pointName='" + pointName + "'," +
                "  pointValue=" + pointValue + "," +
                "  recordTime=" + recordTime + "," +
                "  createTime=" + createTime +
                "}";
    }
}
