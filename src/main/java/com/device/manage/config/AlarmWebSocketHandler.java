package com.device.manage.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Component
public class AlarmWebSocketHandler extends TextWebSocketHandler {

    // 用于存储所有活跃的WebSocket会话
    private static final Set<WebSocketSession> sessions = Collections.synchronizedSet(new HashSet<>());
    
    private final ObjectMapper objectMapper;
    
    public AlarmWebSocketHandler() {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // 连接建立后添加会话到集合
        sessions.add(session);
        System.out.println("WebSocket连接建立，当前连接数: " + sessions.size());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // 连接关闭后从集合中移除会话
        sessions.remove(session);
        System.out.println("WebSocket连接关闭，当前连接数: " + sessions.size());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // 处理客户端发送的消息（如果需要）
        System.out.println("收到客户端消息: " + message.getPayload());
        session.sendMessage(new TextMessage("服务器已收到消息: " + message.getPayload()));
    }

    /**
     * 向所有连接的客户端推送告警消息
     * @param alarm 告警信息
     */
    public void sendAlarmToAllClients(Object alarm) {
        try {
            String jsonAlarm = objectMapper.writeValueAsString(alarm);
            TextMessage message = new TextMessage(jsonAlarm);
            
            // 遍历所有会话并发送消息
            for (WebSocketSession session : sessions) {
                if (session.isOpen()) {
                    session.sendMessage(message);
                }
            }
        } catch (IOException e) {
            System.err.println("推送告警消息失败: " + e.getMessage());
        }
    }

    /**
     * 获取当前活跃连接数
     */
    public int getActiveConnections() {
        return sessions.size();
    }
}