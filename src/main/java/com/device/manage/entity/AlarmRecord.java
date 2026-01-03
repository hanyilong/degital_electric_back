package com.device.manage.entity;

import java.util.Date;

public class AlarmRecord {
    private Long id;
    private Long ruleId;
    private Long deviceCode;
    private String deviceName;
    private Long projectId;
    private Date alarmTime;
    private String alarmLevel;
    private String alarmMessage;
    private Double alarmValue;
    private String status;
    private Date resolveTime;
    private Date createTime;
    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRuleId() {
        return ruleId;
    }

    public void setRuleId(Long ruleId) {
        this.ruleId = ruleId;
    }

    public Long getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(Long deviceCode) {
        this.deviceCode = deviceCode;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Date getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(Date alarmTime) {
        this.alarmTime = alarmTime;
    }

    public String getAlarmLevel() {
        return alarmLevel;
    }

    public void setAlarmLevel(String alarmLevel) {
        this.alarmLevel = alarmLevel;
    }

    public String getAlarmMessage() {
        return alarmMessage;
    }

    public void setAlarmMessage(String alarmMessage) {
        this.alarmMessage = alarmMessage;
    }

    public Double getAlarmValue() {
        return alarmValue;
    }

    public void setAlarmValue(Double alarmValue) {
        this.alarmValue = alarmValue;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getResolveTime() {
        return resolveTime;
    }

    public void setResolveTime(Date resolveTime) {
        this.resolveTime = resolveTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    @Override
    public String toString() {
        return "AlarmRecord{" +
                "id=" + id +
                ", ruleId=" + ruleId +
                ", deviceCode='" + deviceCode + '\'' +
                ", projectId=" + projectId +
                ", alarmTime=" + alarmTime +
                ", alarmLevel='" + alarmLevel + '\'' +
                ", alarmMessage='" + alarmMessage + '\'' +
                ", alarmValue=" + alarmValue +
                ", status='" + status + '\'' +
                ", resolveTime=" + resolveTime +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
