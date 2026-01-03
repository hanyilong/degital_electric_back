package com.device.manage.service;

import com.device.manage.entity.AlarmRule;
import java.util.List;

public interface AlarmRuleService {
    List<AlarmRule> findAll();
    List<AlarmRule> findByProjectId(Long projectId);
    AlarmRule findById(Long id);
    List<AlarmRule> findByModelId(Long modelId);
    List<AlarmRule> findByModelIdAndProjectId(Long modelId, Long projectId);
    List<AlarmRule> findByDeviceCode(String deviceCode);
    List<AlarmRule> findByDeviceCodeAndProjectId(String deviceCode, Long projectId);
    boolean save(AlarmRule alarmRule);
    boolean update(AlarmRule alarmRule);
    boolean deleteById(Long id);
    boolean updateStatus(Long id, Boolean isActive);
}
