package com.device.manage.mapper;

import com.device.manage.entity.TimeSeriesData;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface TimeSeriesDataMapper {
    /**
     * 根据设备ID查询时序数据
     * @param deviceCode 设备ID
     * @return 时序数据列表
     */
    List<TimeSeriesData> selectByDeviceCode(@Param("deviceCode") String deviceCode);

    /**
     * 根据设备ID和点位名称查询时序数据
     * @param deviceCode 设备ID
     * @param pointName 点位名称
     * @return 时序数据列表
     */
    List<TimeSeriesData> selectByDeviceCodeAndPointName(@Param("deviceCode") String deviceCode, @Param("pointName") String pointName);

    /**
     * 根据设备ID和时间范围查询时序数据
     * @param deviceCode 设备ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 时序数据列表
     */
    List<TimeSeriesData> selectByDeviceCodeAndTimeRange(
            @Param("deviceCode") String deviceCode,
            @Param("startTime") Date startTime,
            @Param("endTime") Date endTime
    );

    /**
     * 根据设备ID、点位名称和时间范围查询时序数据
     * @param deviceCode 设备ID
     * @param pointName 点位名称
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 时序数据列表
     */
    List<TimeSeriesData> selectByDeviceCodePointNameAndTimeRange(
            @Param("deviceCode") String deviceCode,
            @Param("pointName") String pointName,
            @Param("startTime") Date startTime,
            @Param("endTime") Date endTime
    );

    /**
     * 插入时序数据
     * @param timeSeriesData 时序数据
     * @return 影响行数
     */
    int insert(TimeSeriesData timeSeriesData);

    /**
     * 查询设备的所有点位名称
     * @param deviceCode 设备ID
     * @return 点位名称列表
     */
    List<String> selectPointNamesByDeviceCode(@Param("deviceCode") String deviceCode);

    /**
     * 执行SQL查询
     * @param sql SQL语句
     * @return 查询结果
     */
    List<Map<String, Object>> sqlQuery(@Param("sql") String sql);

    void updateToNewValue(TimeSeriesData timeSeriesData);

    List<String> selectOnLineDeviceCodes();
}
