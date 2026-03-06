package com.device.manage.service;

import com.device.manage.config.AlarmWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AlarmWebSocketService {

    @Autowired
    private AlarmWebSocketHandler alarmWebSocketHandler;

    /**
     * 向所有连接的客户端推送新告警
     * @param alarm 告警信息
     */
    public void sendAlarm(Object alarm) {
        alarmWebSocketHandler.sendAlarmToAllClients(alarm);
    }
}