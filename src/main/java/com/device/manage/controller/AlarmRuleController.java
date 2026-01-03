package com.device.manage.controller;

import com.device.manage.entity.AlarmRule;
import com.device.manage.service.AlarmRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/alarm-rule")
@CrossOrigin(origins = "*")
public class AlarmRuleController {

    @Autowired
    private AlarmRuleService alarmRuleService;

    @GetMapping
    public List<AlarmRule> getAllAlarmRules(@RequestParam(required = false) Long projectId) {
        if (projectId != null) {
            return alarmRuleService.findByProjectId(projectId);
        }
        return alarmRuleService.findAll();
    }

    @GetMapping("/{id}")
    public AlarmRule getAlarmRuleById(@PathVariable Long id) {
        return alarmRuleService.findById(id);
    }

    @GetMapping("/model/{modelId}")
    public List<AlarmRule> getAlarmRulesByModelId(@PathVariable Long modelId, @RequestParam(required = false) Long projectId) {
        if (projectId != null) {
            return alarmRuleService.findByModelIdAndProjectId(modelId, projectId);
        }
        return alarmRuleService.findByModelId(modelId);
    }

    @GetMapping("/device/{deviceId}")
    public List<AlarmRule> getAlarmRulesByDeviceId(@PathVariable String deviceId, @RequestParam(required = false) Long projectId) {
        if (projectId != null) {
            return alarmRuleService.findByDeviceCodeAndProjectId(deviceId, projectId);
        }
        return alarmRuleService.findByDeviceCode(deviceId);
    }

    @PostMapping
    public Map<String, Object> createAlarmRule(@RequestBody AlarmRule alarmRule) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = alarmRuleService.save(alarmRule);
            if (success) {
                result.put("success", true);
                result.put("message", "告警规则创建成功");
            } else {
                result.put("success", false);
                result.put("message", "告警规则创建失败");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "告警规则创建失败：" + e.getMessage());
            // 处理外键约束错误，返回友好提示
            if (e.getCause() instanceof java.sql.SQLIntegrityConstraintViolationException) {
                java.sql.SQLIntegrityConstraintViolationException sqlException =
                    (java.sql.SQLIntegrityConstraintViolationException) e.getCause();
                if (sqlException.getMessage().contains("alarm_rule_ibfk_1")) {
                    result.put("message", "告警规则创建失败：设备ID不存在");
                } else if (sqlException.getMessage().contains("alarm_rule_ibfk_2")) {
                    result.put("message", "告警规则创建失败：模型ID不存在");
                } else if (sqlException.getMessage().contains("alarm_rule_ibfk_3")) {
                    result.put("message", "告警规则创建失败：项目ID不存在");
                }
            }
        }
        return result;
    }

    @PutMapping("/{id}")
    public boolean updateAlarmRule(@PathVariable Long id, @RequestBody AlarmRule alarmRule) {
        alarmRule.setId(id);
        return alarmRuleService.update(alarmRule);
    }

    @PutMapping("/{id}/status")
    public boolean updateAlarmRuleStatus(@PathVariable Long id, @RequestBody Boolean isActive) {
        return alarmRuleService.updateStatus(id, isActive);
    }

    @DeleteMapping("/{id}")
    public boolean deleteAlarmRule(@PathVariable Long id) {
        return alarmRuleService.deleteById(id);
    }
}
