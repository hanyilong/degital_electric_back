package com.device.manage.mqtt;

import com.device.manage.entity.Device;
import com.device.manage.entity.ThingModel;
import com.device.manage.entity.TimeSeriesData;
import com.device.manage.influxdb.InfluxDBService;
import com.device.manage.service.DeviceService;
import com.device.manage.service.ThingModelService;
import com.device.manage.service.TimeSeriesDataService;
import com.device.manage.service.AlarmRuleService;
import com.device.manage.service.AlarmRecordService;
import com.device.manage.entity.AlarmRule;
import com.device.manage.entity.AlarmRecord;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MqttMessageHandler implements MessageHandler {

    @Autowired
    private TimeSeriesDataService timeSeriesDataService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private ThingModelService thingModelService;

    @Autowired
    private InfluxDBService influxDBService;

    @Autowired
    private AlarmRuleService alarmRuleService;

    @Autowired
    private AlarmRecordService alarmRecordService;
    
    @Value("${time.series.data.service}")
    private String timeSeriesDataServiceType;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 检查告警条件是否满足
     *
     * @param pointValue     当前数据点值
     * @param conditionType  条件类型（如>、<、>=、<=、==、!=）
     * @param thresholdValue 阈值
     * @return 是否触发告警
     */
    private boolean checkAlarmCondition(double pointValue, String conditionType, Double thresholdValue) {
        if (thresholdValue == null) {
            return false;
        }

        switch (conditionType) {
            case ">":
                return pointValue > thresholdValue;
            case ">=":
                return pointValue >= thresholdValue;
            case "<":
                return pointValue < thresholdValue;
            case "<=":
                return pointValue <= thresholdValue;
            case "==":
                return Math.abs(pointValue - thresholdValue) < 0.0001; // 处理浮点数比较
            case "!=":
                return Math.abs(pointValue - thresholdValue) >= 0.0001;
            default:
                System.err.println("Unknown condition type: " + conditionType);
                return false;
        }
    }

    @Override
    public void handleMessage(Message<?> message) throws MessagingException {
        try {
            // 获取MQTT消息内容
            String payload = (String) message.getPayload();
            System.out.println("Received MQTT message: " + payload);

            // 解析JSON消息
            JsonNode root = objectMapper.readTree(payload);

            // 获取设备ID
            String deviceCode = root.path("deviceCode").asText();
            if (deviceCode == null || deviceCode.isEmpty()) {
                System.out.println("Device ID not found in MQTT message");
                return;
            }

            // 获取时间戳
            Date recordTime;
            if (root.has("timestamp")) {
                long timestamp = root.path("timestamp").asLong(System.currentTimeMillis());
                recordTime = new Date(timestamp * 1000);
            } else {
                recordTime = new Date();
            }

            // 获取数据点
            JsonNode dataNode = root.path("data");
            if (dataNode.isObject()) {
                Iterator<String> fieldNames = dataNode.fieldNames();
                Map<String, Double> dataMap = new HashMap<>();

                // 查询该设备的所有激活告警规则
                List<AlarmRule> activeAlarmRules = alarmRuleService.findByDeviceCode(deviceCode);
                // 过滤出激活的规则
                activeAlarmRules = activeAlarmRules.stream()
                        .filter(rule -> Boolean.TRUE.equals(rule.getIsActive()))
                        .collect(Collectors.toList());

                while (fieldNames.hasNext()) {
                    String pointName = fieldNames.next();
                    JsonNode valueNode = dataNode.path(pointName);

                    // 只处理数值类型
                    if (valueNode.isNumber()) {
                        double pointValue = valueNode.asDouble();

                        // 创建时序数据对象
                        TimeSeriesData timeSeriesData = new TimeSeriesData();
                        timeSeriesData.setDeviceCode(deviceCode);
                        timeSeriesData.setPointName(pointName);
                        timeSeriesData.setPointValue(pointValue);
                        timeSeriesData.setRecordTime(recordTime);
                        timeSeriesData.setCreateTime(new Date());

                        // 根据配置选择使用哪个服务保存数据
                        if ("mysql".equalsIgnoreCase(timeSeriesDataServiceType)) {
                            // 保存到MySQL时序表
                            boolean saved = timeSeriesDataService.saveTimeSeriesData(timeSeriesData);
                            if (saved) {
                                System.out.println("Saved time series data to MySQL: deviceCode=" + deviceCode + ", pointName="
                                        + pointName + ", value=" + pointValue);
                            } else {
                                System.out.println("Failed to save time series data to MySQL: deviceCode=" + deviceCode
                                        + ", pointName=" + pointName);
                            }
                        } else if ("influxdb".equalsIgnoreCase(timeSeriesDataServiceType)) {
                            // 添加到数据Map中，用于InfluxDB存储
                            dataMap.put(pointName, pointValue);
                        }

                        // 检查告警规则
                        for (AlarmRule rule : activeAlarmRules) {
                            if (rule.getPointName().equals(pointName)) {
                                // 检查条件是否满足
                                Double thresholdValueDouble = null;
                                try {
                                    thresholdValueDouble = Double.parseDouble(rule.getThresholdValue());
                                } catch (NumberFormatException e) {
                                    System.err.println("Invalid threshold value: " + rule.getThresholdValue());
                                }
                                boolean isAlarmTriggered = checkAlarmCondition(pointValue, rule.getAlarmCondition(), thresholdValueDouble);
                                if (isAlarmTriggered) {
                                    // 生成告警记录
                                    AlarmRecord alarmRecord = new AlarmRecord();
                                    alarmRecord.setRuleId(rule.getId());
                                    alarmRecord.setDeviceCode(Long.parseLong(deviceCode));
                                    alarmRecord.setProjectId(rule.getProjectId());
                                    alarmRecord.setAlarmTime(recordTime);
                                    alarmRecord.setAlarmLevel(rule.getAlarmLevel());
                                    alarmRecord.setAlarmMessage(
                                            rule.getRuleName() + "触发告警：" + pointName + "=" + pointValue);
                                    alarmRecord.setAlarmValue(pointValue);
                                    alarmRecord.setStatus("active");
                                    alarmRecord.setCreateTime(new Date());
                                    alarmRecord.setUpdateTime(new Date());

                                    // 保存告警记录
                                    boolean alarmSaved = alarmRecordService.save(alarmRecord);
                                    if (alarmSaved) {
                                        System.out.println("Generated alarm record: " + alarmRecord);
                                    } else {
                                        System.err.println("Failed to generate alarm record: " + alarmRecord);
                                    }
                                }
                            }
                        }
                    }
                }

                // 将数据存储到InfluxDB（仅当配置为influxdb时执行）
                if ("influxdb".equalsIgnoreCase(timeSeriesDataServiceType) && !dataMap.isEmpty()) {
                    // 根据设备ID查询设备信息
                    Device device = deviceService.findByDeviceCode(deviceCode);

                    // 如果设备不存在，自动创建一个默认设备
                    if (device == null) {
                        System.out.println("Device not found, deviceCode: " + deviceCode);
                    } else {
                        // 查询物模型信息
                        ThingModel thingModel = thingModelService.findById(device.getModelId());
                        if (thingModel != null) {
                            // 使用物模型名称作为modelCode
                            String modelCode = thingModel.getId()+"";
                            // 保存到InfluxDB
                            influxDBService.saveTimeSeriesData(deviceCode, modelCode, dataMap, recordTime);
                            System.out.println("Saved time series data to InfluxDB: deviceCode=" + deviceCode
                                    + ", modelCode=" + modelCode);
                        } else {
                            System.out.println("Thing model not found for device: " + deviceCode);
                        }
                    }
                }
            }

        } catch (Exception e) {
            System.err.println("Error processing MQTT message: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
