package com.device.manage.influxdb;

import com.device.manage.entity.TimeSeriesData;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface InfluxDBService {
    
    /**
     * 保存时序数据到InfluxDB
     * @param deviceId 设备ID
     * @param modelCode 物模型编码
     * @param data 数据点集合
     * @param recordTime 记录时间
     */
    void saveTimeSeriesData(String deviceId, String modelCode, Map<String, Double> data, Date recordTime);
    
    /**
     * 根据设备ID、点位名称和时间范围查询时序数据
     * @param deviceId 设备ID
     * @param pointName 点位名称
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 时序数据列表
     */
    List<TimeSeriesData> queryTimeSeriesData(String deviceId, String pointName, Date startTime, Date endTime);
    
    /**
     * 查询设备的所有点位数据
     * @param deviceId 设备ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 设备的所有点位数据
     */
    Map<String, List<TimeSeriesData>> queryAllPointsData(String deviceId, Date startTime, Date endTime);
    
    /**
     * 执行SQL查询
     * @param sql SQL语句
     * @return 查询结果
     */
    List<Map<String, Object>> sqlQuery(String sql);
}
