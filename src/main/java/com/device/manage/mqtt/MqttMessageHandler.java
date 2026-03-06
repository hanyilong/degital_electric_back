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
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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

    // 线程池配置
    private final ExecutorService executorService;

    // 本地缓存
    private final Map<String, Device> deviceCache = new HashMap<>();
    private final Map<Long, ThingModel> thingModelCache = new HashMap<>();

    // 批量处理缓冲区
    private final Map<String, List<TimeSeriesData>> timeSeriesDataBuffer = new HashMap<>();
    private final Map<String, List<AlarmRecord>> alarmRecordBuffer = new HashMap<>();
    private final Map<String, Map<String, Double>> influxDBDataBuffer = new HashMap<>();
    private final Map<String, Date> deviceLastProcessTime = new HashMap<>();

    // 批量处理阈值和时间间隔
    private static final int BATCH_SIZE = 100;
    private static final long BATCH_INTERVAL_MS = 5000; // 5秒

    public MqttMessageHandler() {
        // 初始化线程池
        this.executorService = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors() * 2,
                r -> {
                    Thread t = new Thread(r);
                    t.setDaemon(true);
                    t.setName("mqtt-message-handler-" + t.getId());
                    return t;
                }
        );

        // 启动批量处理任务
        startBatchProcessingTask();
    }

    /**
     * 启动批量处理定时任务
     */
    private void startBatchProcessingTask() {
        Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r);
            t.setDaemon(true);
            t.setName("batch-processing-task");
            return t;
        }).scheduleAtFixedRate(this::processBatchData, BATCH_INTERVAL_MS, BATCH_INTERVAL_MS, TimeUnit.MILLISECONDS);
    }

    /**
     * 处理批量数据
     */
    private void processBatchData() {
        // 处理时序数据批量写入
        synchronized (timeSeriesDataBuffer) {
            for (Map.Entry<String, List<TimeSeriesData>> entry : timeSeriesDataBuffer.entrySet()) {
                List<TimeSeriesData> dataList = entry.getValue();
                if (!dataList.isEmpty()) {
                    try {
                        timeSeriesDataService.batchSaveTimeSeriesData(dataList);
                        System.out.println("Batch saved " + dataList.size() + " time series data records for device: " + entry.getKey());
                        dataList.clear();
                    } catch (Exception e) {
                        System.err.println("Error in batch saving time series data: " + e.getMessage());
                    }
                }
            }
        }

        // 处理告警记录批量写入
        synchronized (alarmRecordBuffer) {
            for (Map.Entry<String, List<AlarmRecord>> entry : alarmRecordBuffer.entrySet()) {
                List<AlarmRecord> alarmList = entry.getValue();
                if (!alarmList.isEmpty()) {
                    try {
                        alarmRecordService.batchSaveAlarmRecords(alarmList);
                        System.out.println("Batch saved " + alarmList.size() + " alarm records for device: " + entry.getKey());
                        alarmList.clear();
                    } catch (Exception e) {
                        System.err.println("Error in batch saving alarm records: " + e.getMessage());
                    }
                }
            }
        }

        // 处理InfluxDB批量写入
        synchronized (influxDBDataBuffer) {
            for (Map.Entry<String, Map<String, Double>> entry : influxDBDataBuffer.entrySet()) {
                String deviceCode = entry.getKey();
                Map<String, Double> dataMap = entry.getValue();
                if (!dataMap.isEmpty()) {
                    try {
                        // 获取设备信息（使用缓存）
                        Device device = getDeviceFromCache(deviceCode);
                        if (device != null) {
                            // 获取物模型信息（使用缓存）
                            ThingModel thingModel = getThingModelFromCache(device.getModelId());
                            if (thingModel != null) {
                                String modelCode = thingModel.getId() + "";
                                influxDBService.saveTimeSeriesData(deviceCode, modelCode, dataMap, new Date());
                                System.out.println("Batch saved data to InfluxDB for device: " + deviceCode);
                                dataMap.clear();
                            }
                        }
                    } catch (Exception e) {
                        System.err.println("Error in batch saving to InfluxDB: " + e.getMessage());
                    }
                }
            }
        }

        // 清理过期缓存
        cleanupCache();
    }

    /**
     * 从缓存获取设备信息
     */
    private Device getDeviceFromCache(String deviceCode) {
        if (deviceCache.containsKey(deviceCode)) {
            return deviceCache.get(deviceCode);
        }
        Device device = deviceService.findByDeviceCode(deviceCode);
        if (device != null) {
            deviceCache.put(deviceCode, device);
        }
        return device;
    }

    /**
     * 从缓存获取物模型信息
     */
    private ThingModel getThingModelFromCache(Long modelId) {
        if (modelId == null) return null;
        if (thingModelCache.containsKey(modelId)) {
            return thingModelCache.get(modelId);
        }
        ThingModel thingModel = thingModelService.findById(modelId);
        if (thingModel != null) {
            thingModelCache.put(modelId, thingModel);
        }
        return thingModel;
    }

    /**
     * 清理过期缓存
     */
    private void cleanupCache() {
        long now = System.currentTimeMillis();
        // 清理设备缓存（1小时过期）
        deviceCache.entrySet().removeIf(entry -> {
            Device device = entry.getValue();
            return device == null || (now - deviceLastProcessTime.getOrDefault(entry.getKey(), new Date(0)).getTime() > 3600000);
        });
        // 清理物模型缓存（24小时过期）
        thingModelCache.entrySet().removeIf(entry -> {
            // 简单清理策略，实际项目中可以使用更复杂的缓存过期策略
            return false;
        });
    }

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
        // 提交到线程池异步处理
        executorService.submit(() -> {
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

                // 更新设备最后处理时间
                deviceLastProcessTime.put(deviceCode, new Date());

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

                    // 查询该设备的所有激活告警规则（只查询一次）
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

                            // 创建时序数据对象并添加到缓冲区
                            TimeSeriesData timeSeriesData = new TimeSeriesData();
                            timeSeriesData.setDeviceCode(deviceCode);
                            timeSeriesData.setPointName(pointName);
                            timeSeriesData.setPointValue(pointValue);
                            timeSeriesData.setRecordTime(recordTime);
                            timeSeriesData.setCreateTime(new Date());

                            // 添加到时序数据缓冲区
                            addToTimeSeriesDataBuffer(deviceCode, timeSeriesData);

                            dataMap.put(pointName, pointValue);

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

                                        // 添加到告警记录缓冲区
                                        addToAlarmRecordBuffer(deviceCode, alarmRecord);
                                    }
                                }
                            }
                        }
                    }

                    // 将数据添加到InfluxDB缓冲区（仅当配置为influxdb时执行）
                    if ("influxdb".equalsIgnoreCase(timeSeriesDataServiceType) && !dataMap.isEmpty()) {
                        addToInfluxDBBuffer(deviceCode, dataMap);
                    }
                }

            } catch (Exception e) {
                System.err.println("Error processing MQTT message: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    /**
     * 添加到时序数据缓冲区
     */
    private void addToTimeSeriesDataBuffer(String deviceCode, TimeSeriesData data) {
        synchronized (timeSeriesDataBuffer) {
            timeSeriesDataBuffer.computeIfAbsent(deviceCode, k -> new ArrayList<>()).add(data);
            // 检查是否达到批量处理阈值
            List<TimeSeriesData> dataList = timeSeriesDataBuffer.get(deviceCode);
            if (dataList.size() >= BATCH_SIZE) {
                // 达到阈值，立即处理
                try {
                    timeSeriesDataService.batchSaveTimeSeriesData(dataList);
                    System.out.println("Batch saved " + dataList.size() + " time series data records for device: " + deviceCode);
                    dataList.clear();
                } catch (Exception e) {
                    System.err.println("Error in batch saving time series data: " + e.getMessage());
                }
            }
        }
    }

    /**
     * 添加到告警记录缓冲区
     */
    private void addToAlarmRecordBuffer(String deviceCode, AlarmRecord record) {
        synchronized (alarmRecordBuffer) {
            alarmRecordBuffer.computeIfAbsent(deviceCode, k -> new ArrayList<>()).add(record);
            // 检查是否达到批量处理阈值
            List<AlarmRecord> recordList = alarmRecordBuffer.get(deviceCode);
            if (recordList.size() >= BATCH_SIZE) {
                // 达到阈值，立即处理
                try {
                    alarmRecordService.batchSaveAlarmRecords(recordList);
                    System.out.println("Batch saved " + recordList.size() + " alarm records for device: " + deviceCode);
                    recordList.clear();
                } catch (Exception e) {
                    System.err.println("Error in batch saving alarm records: " + e.getMessage());
                }
            }
        }
    }

    /**
     * 添加到InfluxDB数据缓冲区
     */
    private void addToInfluxDBBuffer(String deviceCode, Map<String, Double> dataMap) {
        synchronized (influxDBDataBuffer) {
            Map<String, Double> bufferMap = influxDBDataBuffer.computeIfAbsent(deviceCode, k -> new HashMap<>());
            // 合并数据
            bufferMap.putAll(dataMap);
            // 检查是否达到批量处理阈值
            if (bufferMap.size() >= BATCH_SIZE) {
                // 达到阈值，立即处理
                try {
                    // 获取设备信息（使用缓存）
                    Device device = getDeviceFromCache(deviceCode);
                    if (device != null) {
                        // 获取物模型信息（使用缓存）
                        ThingModel thingModel = getThingModelFromCache(device.getModelId());
                        if (thingModel != null) {
                            String modelCode = thingModel.getId() + "";
                            influxDBService.saveTimeSeriesData(deviceCode, modelCode, bufferMap, new Date());
                            System.out.println("Batch saved data to InfluxDB for device: " + deviceCode);
                            bufferMap.clear();
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Error in batch saving to InfluxDB: " + e.getMessage());
                }
            }
        }
    }
}
