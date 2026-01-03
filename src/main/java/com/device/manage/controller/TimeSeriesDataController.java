package com.device.manage.controller;

import com.device.manage.entity.TimeSeriesData;
import com.device.manage.influxdb.InfluxDBService;
import com.device.manage.service.TimeSeriesDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.text.SimpleDateFormat;

@RestController
@RequestMapping("/time-series")
@CrossOrigin(origins = "*")
public class TimeSeriesDataController {

    @Autowired
    private TimeSeriesDataService timeSeriesDataService;

    @Autowired
    private InfluxDBService influxDBService;
    
    @Value("${time.series.data.service}")
    private String timeSeriesDataServiceType;

    /**
     * 根据设备ID查询时序数据
     * @param deviceCode 设备ID
     * @return 时序数据列表
     */
    @GetMapping("/device/{deviceCode}")
    public List<TimeSeriesData> getByDeviceCode(@PathVariable String deviceCode) {
        return timeSeriesDataService.getByDeviceCode(deviceCode);
    }

    /**
     * 根据设备ID和点位名称查询时序数据
     * @param deviceCode 设备ID
     * @param pointName 点位名称
     * @return 时序数据列表
     */
    @GetMapping("/device/{deviceCode}/point/{pointName}")
    public List<TimeSeriesData> getByDeviceCodeAndPointName(
            @PathVariable String deviceCode,
            @PathVariable String pointName) {
        return timeSeriesDataService.getByDeviceCodeAndPointName(deviceCode, pointName);
    }

    /**
     * 根据设备ID和时间范围查询时序数据
     * @param deviceCode 设备ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 时序数据列表
     */
    @GetMapping("/device/{deviceCode}/time-range")
    public List<TimeSeriesData> getByDeviceCodeAndTimeRange(
            @PathVariable String deviceCode,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startTime,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime) {
        return timeSeriesDataService.getByDeviceCodeAndTimeRange(deviceCode, startTime, endTime);
    }

    /**
     * 根据设备ID、点位名称和时间范围查询时序数据
     * @param deviceCode 设备ID
     * @param pointName 点位名称
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 时序数据列表
     */
    @GetMapping("/device/{deviceCode}/point/{pointName}/time-range")
    public List<TimeSeriesData> getByDeviceCodePointNameAndTimeRange(
            @PathVariable String deviceCode,
            @PathVariable String pointName,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startTime,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime) {
        return timeSeriesDataService.getByDeviceCodePointNameAndTimeRange(deviceCode, pointName, startTime, endTime);
    }

    /**
     * 查询设备的所有点位名称（从MySQL）
     * @param deviceCode 设备ID
     * @return 点位名称列表
     */
    @GetMapping("/device/{deviceCode}/points")
    public List<String> getPointNamesByDeviceCode(@PathVariable String deviceCode) {
        return timeSeriesDataService.getPointNamesByDeviceCode(deviceCode);
    }

    /**
     * 查询设备的所有点位名称（从InfluxDB）
     * @param deviceCode 设备ID
     * @return 点位名称列表
     */
    @GetMapping("/influxdb/device/{deviceCode}/points")
    public List<String> getPointNamesFromInfluxDB(@PathVariable String deviceCode) {
        // 由于InfluxDBService没有直接提供获取点位名称的方法，
        // 我们可以通过查询所有点位数据并提取点位名称来实现
        Date startTime = new Date(System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000); // 最近7天
        Date endTime = new Date();
        Map<String, List<TimeSeriesData>> dataMap = influxDBService.queryAllPointsData(deviceCode, startTime, endTime);
        return new ArrayList<>(dataMap.keySet());
    }

    /**
     * 保存时序数据
     * @param timeSeriesData 时序数据
     * @return 保存结果
     */
    @PostMapping
    public boolean saveTimeSeriesData(@RequestBody TimeSeriesData timeSeriesData) {
        return timeSeriesDataService.saveTimeSeriesData(timeSeriesData);
    }

    /**
     * 从InfluxDB根据设备ID和点位名称查询时序数据
     * @param deviceCode 设备ID
     * @param pointName 点位名称
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 时序数据列表
     */
    @GetMapping("/influxdb/device/{deviceCode}/point/{pointName}/time-range")
    public List<TimeSeriesData> getFromInfluxDBByDeviceCodePointNameAndTimeRange(
            @PathVariable String deviceCode,
            @PathVariable String pointName,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startTime,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime) {
        return influxDBService.queryTimeSeriesData(deviceCode, pointName, startTime, endTime);
    }

    /**
     * 从InfluxDB查询设备的所有点位数据
     * @param deviceCode 设备ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 设备的所有点位数据
     */
    @GetMapping("/influxdb/device/{deviceCode}/time-range")
    public List<TimeSeriesData> getFromInfluxDBByDeviceCodeAndTimeRange(
            @PathVariable String deviceCode,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startTime,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime) {
        Map<String, List<TimeSeriesData>> dataMap = influxDBService.queryAllPointsData(deviceCode, startTime, endTime);
        List<TimeSeriesData> result = new ArrayList<>();
        // 将Map中的所有数据转换为List
        for (List<TimeSeriesData> dataList : dataMap.values()) {
            result.addAll(dataList);
        }
        // 按时间排序
        result.sort(Comparator.comparing(TimeSeriesData::getRecordTime));
        return result;
    }

    /**
     * SQL查询请求参数
     */
    public static class SqlQueryRequest {
        private String sql;
        private String startTime;
        private String endTime;

        // getter和setter方法
        public String getSql() {
            return sql;
        }

        public void setSql(String sql) {
            this.sql = sql;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }
    }

    /**
     * 执行SQL查询
     * @param request SQL查询请求参数
     * @return 查询结果
     */
    @PostMapping("/sql-query")
    public Map<String, Object> sqlQuery(@RequestBody SqlQueryRequest request) {
        // 根据配置选择使用哪个服务
        List<Map<String, Object>> queryResult;
        if ("influxdb".equalsIgnoreCase(timeSeriesDataServiceType)) {
            queryResult = influxDBService.sqlQuery(request.getSql());
        } else {
            queryResult = timeSeriesDataService.sqlQuery(request.getSql());
        }
        
        // 转换结果格式为包含field和data的结构
        Map<String, Object> result = new HashMap<>();
        
        if (queryResult == null || queryResult.isEmpty()) {
            result.put("field", new ArrayList<>());
            result.put("data", new ArrayList<>());
            return result;
        }
        
        // 获取字段名
        List<String> fields = new ArrayList<>(queryResult.get(0).keySet());
        result.put("field", fields);
        
        // 转换数据部分
        List<List<Object>> data = new ArrayList<>();
        for (Map<String, Object> row : queryResult) {
            List<Object> rowData = new ArrayList<>();
            for (String field : fields) {
                Object value = row.get(field);
                // 对时间字段进行格式化
                if ("time".equalsIgnoreCase(field)) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    if (value instanceof Date) {
                        rowData.add(sdf.format((Date) value));
                    } else if (value instanceof Number) {
                        // 处理long类型的时间戳（毫秒级）
                        rowData.add(sdf.format(new Date(((Number) value).longValue())));
                    } else {
                        rowData.add(value);
                    }
                } else {
                    rowData.add(value);
                }
            }
            data.add(rowData);
        }
        
        result.put("data", data);
        return result;
    }
}
