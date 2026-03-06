package com.device.manage.controller;

import com.device.manage.entity.AlarmRecord;
import com.device.manage.service.AlarmWebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class TestWebSocketController {

    @Autowired
    private AlarmWebSocketService alarmWebSocketService;

    /**
     * 测试WebSocket推送告警
     */
    @PostMapping("/api/test-websocket")
    public String testWebSocket() {
        // 创建测试告警
        AlarmRecord testAlarm = new AlarmRecord();
        testAlarm.setId(100L);
        testAlarm.setDeviceId("test-device-001");
        testAlarm.setDeviceName("测试设备");
        testAlarm.setAlarmType("温度异常");
        testAlarm.setLevel("high");
        testAlarm.setMessage("测试告警：温度超过阈值");
        testAlarm.setTime(new Date());
        testAlarm.setStatus("unresolved");
        testAlarm.setUuid("test-uuid-123456");
        testAlarm.setProjectId(1L);
        testAlarm.setRuleId(1L);

        // 推送告警
        alarmWebSocketService.sendAlarm(testAlarm);
        
        return "WebSocket推送测试完成";
    }
}