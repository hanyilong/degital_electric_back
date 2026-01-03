package com.device.manage.service;

import com.device.manage.entity.AlarmRecord;
import java.util.List;
import java.util.Map;

public interface AlarmRecordService {
    List<AlarmRecord> findAll();
    List<AlarmRecord> findByProjectId(Long projectId);
    AlarmRecord findById(Long id);
    List<AlarmRecord> findByDeviceId(String deviceId);
    List<AlarmRecord> findByDeviceIdAndProjectId(String deviceId, Long projectId);
    List<AlarmRecord> findByRuleId(Long ruleId);
    List<AlarmRecord> findByRuleIdAndProjectId(Long ruleId, Long projectId);
    List<AlarmRecord> findByStatus(String status);
    List<AlarmRecord> findByStatusAndProjectId(String status, Long projectId);
    List<AlarmRecord> findByConditions(Map<String, Object> conditions);
    boolean save(AlarmRecord alarmRecord);
    boolean updateStatus(Long id, String status, String resolveTime);
    boolean deleteById(Long id);
}