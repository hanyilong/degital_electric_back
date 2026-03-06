package com.device.manage.service.impl;

import com.device.manage.entity.AlarmRecord;
import com.device.manage.entity.AlarmStaticByDescription;
import com.device.manage.entity.AlarmStaticByHour;
import com.device.manage.mapper.AlarmRecordMapper;
import com.device.manage.service.AlarmRecordService;
import com.device.manage.service.AlarmWebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AlarmRecordServiceImpl implements AlarmRecordService {

    @Autowired
    private AlarmRecordMapper alarmRecordMapper;

    @Autowired
    private AlarmWebSocketService alarmWebSocketService;

    @Override
    public List<AlarmRecord> findAll() {
        return alarmRecordMapper.selectAll();
    }

    @Override
    public List<AlarmRecord> findByProjectId(Long projectId) {
        return alarmRecordMapper.selectByProjectId(projectId);
    }

    @Override
    public AlarmRecord findById(Long id) {
        return alarmRecordMapper.selectById(id);
    }

    @Override
    public List<AlarmRecord> findByDeviceId(String deviceId) {
        return alarmRecordMapper.selectByDeviceId(deviceId);
    }

    @Override
    public List<AlarmRecord> findByDeviceIdAndProjectId(String deviceId, Long projectId) {
        Map<String, Object> params = new HashMap<>();
        params.put("deviceId", deviceId);
        params.put("projectId", projectId);
        return alarmRecordMapper.selectByDeviceIdAndProjectId(params);
    }

    @Override
    public List<AlarmRecord> findByRuleId(Long ruleId) {
        return alarmRecordMapper.selectByRuleId(ruleId);
    }

    @Override
    public List<AlarmRecord> findByRuleIdAndProjectId(Long ruleId, Long projectId) {
        Map<String, Object> params = new HashMap<>();
        params.put("ruleId", ruleId);
        params.put("projectId", projectId);
        return alarmRecordMapper.selectByRuleIdAndProjectId(params);
    }

    @Override
    public List<AlarmRecord> findByStatus(String status) {
        return alarmRecordMapper.selectByStatus(status);
    }

    @Override
    public List<AlarmRecord> findByStatusAndProjectId(String status, Long projectId) {
        Map<String, Object> params = new HashMap<>();
        params.put("status", status);
        params.put("projectId", projectId);
        return alarmRecordMapper.selectByStatusAndProjectId(params);
    }

    @Override
    public List<AlarmRecord> findByConditions(Map<String, Object> conditions) {
        return alarmRecordMapper.selectByConditions(conditions);
    }

    @Override
    public boolean save(AlarmRecord alarmRecord) {
        int id = alarmRecordMapper.insert(alarmRecord);
        if (id > 0) {
            alarmRecord.setId(Long.valueOf(id));
            // 保存成功后，通过WebSocket推送告警信息
            alarmWebSocketService.sendAlarm(alarmRecord);
        }
        return id > 0;
    }

    @Override
    public boolean updateStatus(Long id, String status, String resolveTime) {
        return alarmRecordMapper.updateStatus(id, status, resolveTime) > 0;
    }

    @Override
    public boolean deleteById(Long id) {
        return alarmRecordMapper.deleteById(id) > 0;
    }

    @Override
    public List<AlarmStaticByHour> findAlarmStaticByHour(Long projectId) {
        return alarmRecordMapper.selectAlarmStaticByHour(projectId);
    }

    @Override
    public List<AlarmStaticByDescription> findAlarmStaticByDescription(Long projectId) {
        return alarmRecordMapper.selectAlarmStaticByDescription(projectId);
    }

    @Override
    public boolean batchSaveAlarmRecords(List<AlarmRecord> alarmRecordList) {
        if (alarmRecordList == null || alarmRecordList.isEmpty()) {
            return false;
        }

        int successCount = 0;
        for (AlarmRecord alarmRecord : alarmRecordList) {
            if (alarmRecord == null) {
                continue;
            }

            try {
                int id = alarmRecordMapper.insert(alarmRecord);
                if (id > 0) {
                    alarmRecord.setId(Long.valueOf(id));
                    // 保存成功后，通过WebSocket推送告警信息
                    alarmWebSocketService.sendAlarm(alarmRecord);
                    successCount++;
                }
            } catch (Exception e) {
                System.err.println("Error in batch saving alarm record: " + e.getMessage());
            }
        }

        return successCount > 0;
    }
}
