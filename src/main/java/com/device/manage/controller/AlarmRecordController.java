package com.device.manage.controller;

import com.device.manage.entity.AlarmRecord;
import com.device.manage.service.AlarmRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/alarm-record")
@CrossOrigin(origins = "*")
public class AlarmRecordController {

    @Autowired
    private AlarmRecordService alarmRecordService;

    @GetMapping
    public List<AlarmRecord> getAllAlarmRecords(@RequestParam(required = false) Long projectId) {
        if (projectId != null) {
            return alarmRecordService.findByProjectId(projectId);
        }
        return alarmRecordService.findAll();
    }

    @GetMapping("/{id}")
    public AlarmRecord getAlarmRecordById(@PathVariable Long id) {
        return alarmRecordService.findById(id);
    }

    @GetMapping("/device/{deviceId}")
    public List<AlarmRecord> getAlarmRecordsByDeviceId(@PathVariable String deviceId, @RequestParam(required = false) Long projectId) {
        if (projectId != null) {
            return alarmRecordService.findByDeviceIdAndProjectId(deviceId, projectId);
        }
        return alarmRecordService.findByDeviceId(deviceId);
    }

    @GetMapping("/rule/{ruleId}")
    public List<AlarmRecord> getAlarmRecordsByRuleId(@PathVariable Long ruleId, @RequestParam(required = false) Long projectId) {
        if (projectId != null) {
            return alarmRecordService.findByRuleIdAndProjectId(ruleId, projectId);
        }
        return alarmRecordService.findByRuleId(ruleId);
    }

    @GetMapping("/status/{status}")
    public List<AlarmRecord> getAlarmRecordsByStatus(@PathVariable String status, @RequestParam(required = false) Long projectId) {
        if (projectId != null) {
            return alarmRecordService.findByStatusAndProjectId(status, projectId);
        }
        return alarmRecordService.findByStatus(status);
    }

    @PostMapping("/search")
    public List<AlarmRecord> searchAlarmRecords(@RequestBody Map<String, Object> conditions) {
        List<AlarmRecord> rlt =  alarmRecordService.findByConditions(conditions);
        return rlt;
    }

    @PostMapping
    public boolean createAlarmRecord(@RequestBody AlarmRecord alarmRecord) {
        return alarmRecordService.save(alarmRecord);
    }

    @PutMapping("/{id}/status")
    public boolean updateAlarmRecordStatus(@PathVariable Long id, @RequestBody Map<String, Object> params) {
        String status = (String) params.get("status");
        String resolveTime = (String) params.get("resolveTime");
        return alarmRecordService.updateStatus(id, status, resolveTime);
    }

    @DeleteMapping("/{id}")
    public boolean deleteAlarmRecord(@PathVariable Long id) {
        return alarmRecordService.deleteById(id);
    }
}
