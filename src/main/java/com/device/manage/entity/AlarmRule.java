package com.device.manage.entity;

import java.util.Date;

public class AlarmRule {
    private Long id;
    private String ruleName;
    private Long modelId;
    private String deviceCode;
    private Long projectId;
    private String pointName;
    private String alarmType;
    private String alarmCondition;
    // conditionType 作为 alarmCondition 的别名，用于兼容旧参数
    private String conditionType;
    private String thresholdValue;
    private String alarmLevel;
    private Boolean isActive;
    private Date createTime;
    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public Long getModelId() {
        return modelId;
    }

    public void setModelId(Long modelId) {
        this.modelId = modelId;
    }

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(Object deviceCode) {
        if (deviceCode instanceof String) {
            this.deviceCode = (String)deviceCode;
        } else if (deviceCode instanceof Long) {
            this.deviceCode = (String) deviceCode;
        } else if (deviceCode instanceof Integer) {
            this.deviceCode = (String) deviceCode;
        }
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Object projectId) {
        if (projectId instanceof String) {
            this.projectId = Long.parseLong((String) projectId);
        } else if (projectId instanceof Long) {
            this.projectId = (Long) projectId;
        } else if (projectId instanceof Integer) {
            this.projectId = ((Integer) projectId).longValue();
        }
    }

    public String getPointName() {
        return pointName;
    }

    public void setPointName(String pointName) {
        this.pointName = pointName;
    }

    public String getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(String alarmType) {
        this.alarmType = alarmType;
    }

    public String getAlarmCondition() {
        return alarmCondition;
    }

    public void setAlarmCondition(String alarmCondition) {
        this.alarmCondition = alarmCondition;
    }

    public String getConditionType() {
        return alarmCondition;
    }

    public void setConditionType(String conditionType) {
        this.conditionType = conditionType;
        // 自动将 conditionType 的值设置到 alarmCondition
        this.alarmCondition = conditionType;
    }

    public String getThresholdValue() {
        return thresholdValue;
    }

    public void setThresholdValue(Object thresholdValue) {
        if (thresholdValue instanceof String) {
            this.thresholdValue = (String) thresholdValue;
        } else if (thresholdValue instanceof Number) {
            this.thresholdValue = thresholdValue.toString();
        }
    }

    public String getAlarmLevel() {
        return alarmLevel;
    }

    public void setAlarmLevel(String alarmLevel) {
        this.alarmLevel = alarmLevel;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
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

    @Override
    public String toString() {
        return "AlarmRule{" +
                "id=" + id +
                ", ruleName='" + ruleName + '\'' +
                ", modelId=" + modelId +
                ", deviceCode=" + deviceCode +
                ", projectId=" + projectId +
                ", pointName='" + pointName + '\'' +
                ", alarmType='" + alarmType + '\'' +
                ", alarmCondition='" + alarmCondition + '\'' +
                ", conditionType='" + conditionType + '\'' +
                ", thresholdValue='" + thresholdValue + '\'' +
                ", alarmLevel='" + alarmLevel + '\'' +
                ", isActive=" + isActive +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
