package com.device.manage.service;

import com.device.manage.entity.TimeSeriesData;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface TimeSeriesDataService {
    /**
     * 根据设备ID查询时序数据
     * @param deviceCode 设备ID
     * @return 时序数据列表
     */
    List<TimeSeriesData> getByDeviceCode(String deviceCode);

    /**
     * 根据设备ID和点位名称查询时序数据
     * @param deviceCode 设备ID
     * @param pointName 点位名称
     * @return 时序数据列表
     */
    List<TimeSeriesData> getByDeviceCodeAndPointName(String deviceCode, String pointName);

    /**
     * 根据设备ID和时间范围查询时序数据
     * @param deviceCode 设备ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 时序数据列表
     */
    List<TimeSeriesData> getByDeviceCodeAndTimeRange(String deviceCode, Date startTime, Date endTime);

    /**
     * 根据设备ID、点位名称和时间范围查询时序数据
     * @param deviceCode 设备ID
     * @param pointName 点位名称
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 时序数据列表
     */
    List<TimeSeriesData> getByDeviceCodePointNameAndTimeRange(String deviceCode, String pointName, Date startTime, Date endTime);

    /**
     * 保存时序数据
     * @param timeSeriesData 时序数据
     * @return 保存结果
     */
    boolean saveTimeSeriesData(TimeSeriesData timeSeriesData);

    /**
     * 查询设备的所有点位名称
     * @param deviceCode 设备ID
     * @return 点位名称列表
     */
    List<String> getPointNamesByDeviceCode(String deviceCode);

    /**
     * 执行SQL查询
     * @param sql SQL语句
     * @return 查询结果
     */
    List<Map<String, Object>> sqlQuery(String sql);

    List<String> findOfflineDeviceCodes();

    /**
     * 批量保存时序数据
     * @param timeSeriesDataList 时序数据列表
     * @return 保存结果
     */
    boolean batchSaveTimeSeriesData(List<TimeSeriesData> timeSeriesDataList);
}
