package com.device.manage.mqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Random;

/**
 * MQTT JUnit测试程序，用于模拟设备上报数据
 */
public class MqttJUnitTest {

    @Test
    public void testMqttPublish() {
        // MQTT配置
        String broker = "tcp://light.lkennicity.com:1883";
        String clientId = "test-client-" + System.currentTimeMillis();
        String topic = "device/data";
        String username = "lamp";
        String password = "Hanyilong1!";

        try {
            // 创建MQTT客户端
            MqttClient mqttClient = new MqttClient(broker, clientId, new MemoryPersistence());

            // 配置连接选项
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            connOpts.setUserName(username);
            connOpts.setPassword(password.toCharArray());

            // 连接到MQTT broker
            System.out.println("Connecting to broker: " + broker);
            mqttClient.connect(connOpts);
            System.out.println("Connected");

            // 模拟设备数据
            String deviceId = "t1";
            long timestamp = System.currentTimeMillis();

            // 构建JSON消息
            String payload = String.format("""
                {
                  "deviceId": "%s",
                  "timestamp": %d,
                  "data": {
                    "temperature": %.1f,
                    "humidity": %.1f,
                    "pressure": %.1f
                  }
                }
                """,
                deviceId, timestamp, 25.5, 65.2, 1013.25);

            // 创建MQTT消息
            MqttMessage message = new MqttMessage(payload.getBytes());
            message.setQos(0);

            // 发布消息
            System.out.println("Publishing message: " + payload);
            mqttClient.publish(topic, message);
            System.out.println("Message published successfully");

            // 断开连接
            mqttClient.disconnect();
            System.out.println("Disconnected");

        } catch (MqttException me) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
        }
    }

    @Test
    public void testMqttPublishDailyData() {
        // MQTT配置
        String broker = "tcp://light.lkennicity.com:1883";
        String clientId = "test-client-" + System.currentTimeMillis();
        String topic = "device/data";
        String username = "lamp";
        String password = "Hanyilong1!";
        String deviceCode = "1007";

        try {
            // 创建MQTT客户端
            MqttClient mqttClient = new MqttClient(broker, clientId, new MemoryPersistence());

            // 配置连接选项
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            connOpts.setUserName(username);
            connOpts.setPassword(password.toCharArray());

            // 连接到MQTT broker
            System.out.println("Connecting to broker: " + broker);
            mqttClient.connect(connOpts);
            System.out.println("Connected");

            // 随机数生成器
            Random random = new Random();

            // 获取今天的开始时间（00:00:00）和结束时间（23:59:00）
            LocalDateTime startOfDay = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
            LocalDateTime endOfDay = LocalDateTime.now().withHour(23).withMinute(59).withSecond(0).withNano(0);

            // 转换为Unix时间戳（秒）
            long startTime = startOfDay.toEpochSecond(ZoneOffset.ofHours(8));
            long endTime = endOfDay.toEpochSecond(ZoneOffset.ofHours(8));

            // 每分钟发送一条数据
            int messageCount = 0;
            for (long timestamp = startTime; timestamp <= endTime; timestamp += 60) {
                // 生成1-100之间的随机值
                double temperature = 1 + random.nextDouble() * 99;
                double humidity = 1 + random.nextDouble() * 99;
                double pressure = 1 + random.nextDouble() * 99;

                // 构建JSON消息
                String payload = String.format("""
                    {
                      "deviceCode": "%s",
                      "timestamp": %d,
                      "data": {
                        "temperature": %.2f,
                        "humidity": %.2f,
                        "pressure": %.2f
                      }
                    }
                    """,
                    deviceCode, timestamp, temperature, humidity, pressure);

                // 创建MQTT消息
                MqttMessage message = new MqttMessage(payload.getBytes());
                message.setQos(0);

                // 发布消息
                mqttClient.publish(topic, message);
                messageCount++;

                // 每10条消息打印一次进度
                if (messageCount % 10 == 0) {
                    System.out.printf("Published %d messages so far...\n", messageCount);
                }

                // 短暂延迟，避免发送过快
                Thread.sleep(10);
            }

            System.out.printf("Total %d messages published successfully\n", messageCount);

            // 断开连接
            mqttClient.disconnect();
            System.out.println("Disconnected");

        } catch (MqttException me) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
        } catch (InterruptedException e) {
            System.out.println("Thread interrupted: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}