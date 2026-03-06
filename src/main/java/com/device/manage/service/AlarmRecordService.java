package com.device.manage.service;

import com.device.manage.entity.AlarmRecord;
import com.device.manage.entity.AlarmStaticByDescription;
import com.device.manage.entity.AlarmStaticByHour;

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

    List<AlarmStaticByHour> findAlarmStaticByHour(Long projectId);

    List<AlarmStaticByDescription> findAlarmStaticByDescription(Long projectId);

    /**
     * 批量保存告警记录
     * @param alarmRecordList 告警记录列表
     * @return 保存结果
     */
    boolean batchSaveAlarmRecords(List<AlarmRecord> alarmRecordList);
}
