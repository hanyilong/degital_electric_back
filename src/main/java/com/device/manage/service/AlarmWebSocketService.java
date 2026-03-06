package com.device.manage.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class AlarmWebSocketService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    /**
     * 向所有连接的客户端推送新告警
     * @param alarm 告警信息
     */
    public void sendAlarm(Object alarm) {
        messagingTemplate.convertAndSend("/topic/alarms", alarm);
    }
}