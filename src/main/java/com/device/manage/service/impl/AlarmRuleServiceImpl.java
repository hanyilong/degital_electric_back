package com.device.manage.service.impl;

import com.device.manage.entity.AlarmRule;
import com.device.manage.entity.Device;
import com.device.manage.mapper.AlarmRuleMapper;
import com.device.manage.mapper.DeviceMapper;
import com.device.manage.service.AlarmRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AlarmRuleServiceImpl implements AlarmRuleService {

    @Autowired
    private AlarmRuleMapper alarmRuleMapper;

    @Autowired
    private DeviceMapper deviceMapper;

    @Override
    public List<AlarmRule> findAll() {
        return alarmRuleMapper.selectAll();
    }

    @Override
    public List<AlarmRule> findByProjectId(Long projectId) {
        return alarmRuleMapper.selectByProjectId(projectId);
    }

    @Override
    public AlarmRule findById(Long id) {
        return alarmRuleMapper.selectById(id);
    }

    @Override
    public List<AlarmRule> findByModelId(Long modelId) {
        return alarmRuleMapper.selectByModelId(modelId);
    }

    @Override
    public List<AlarmRule> findByModelIdAndProjectId(Long modelId, Long projectId) {
        Map<String, Object> params = new HashMap<>();
        params.put("modelId", modelId);
        params.put("projectId", projectId);
        return alarmRuleMapper.selectByModelIdAndProjectId(params);
    }

    @Override
    public List<AlarmRule> findByDeviceCode(String deviceCode) {
        List<AlarmRule> rules = alarmRuleMapper.selectByDeviceCode(deviceCode);
        if (rules != null && !rules.isEmpty()) {
            return rules;
        }
        Device device = deviceMapper.selectByDeviceCode(deviceCode);
        return alarmRuleMapper.selectByModelId(device.getModelId());
    }

    @Override
    public List<AlarmRule> findByDeviceCodeAndProjectId(String deviceCode, Long projectId) {
        Map<String, Object> params = new HashMap<>();
        params.put("deviceId", deviceCode);
        params.put("projectId", projectId);
        return alarmRuleMapper.selectByDeviceCodeAndProjectId(params);
    }

    @Override
    public boolean save(AlarmRule alarmRule) {
        if (alarmRule.getAlarmType() == null || alarmRule.getAlarmType().isEmpty()) {
            // 设置默认告警类型为温度告警
            alarmRule.setAlarmType("temperature_alarm");
        }

        return alarmRuleMapper.insert(alarmRule) > 0;
    }

    /**
     * 生成默认规则编码
     */
    private String generateDefaultRuleCode(String ruleName) {
        // 简单实现：取规则名称的每个汉字的首字母，转换为大写，加上时间戳
        if (ruleName == null || ruleName.isEmpty()) {
            return "RULE_" + System.currentTimeMillis();
        }

        StringBuilder codeBuilder = new StringBuilder();
        for (char c : ruleName.toCharArray()) {
            if (c >= '\u4e00' && c <= '\u9fa5') {
                // 简单处理，直接取首字母（实际项目中应使用拼音库）
                codeBuilder.append(String.valueOf(c).toUpperCase().charAt(0));
            } else {
                codeBuilder.append(String.valueOf(c).toUpperCase());
            }
        }

        return codeBuilder.toString() + "_" + System.currentTimeMillis();
    }

    @Override
    public boolean update(AlarmRule alarmRule) {
        return alarmRuleMapper.update(alarmRule) > 0;
    }

    @Override
    public boolean deleteById(Long id) {
        return alarmRuleMapper.deleteById(id) > 0;
    }

    @Override
    public boolean updateStatus(Long id, Boolean isActive) {
        return alarmRuleMapper.updateStatus(id, isActive) > 0;
    }
}
