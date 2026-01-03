package com.device.manage.service.impl;

import com.device.manage.entity.TimeSeriesData;
import com.device.manage.mapper.TimeSeriesDataMapper;
import com.device.manage.service.TimeSeriesDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class TimeSeriesDataServiceImpl implements TimeSeriesDataService {

    @Autowired
    private TimeSeriesDataMapper timeSeriesDataMapper;

    @Override
    public List<TimeSeriesData> getByDeviceCode(String deviceCode) {
        return timeSeriesDataMapper.selectByDeviceCode(deviceCode);
    }

    @Override
    public List<TimeSeriesData> getByDeviceCodeAndPointName(String deviceCode, String pointName) {
        return timeSeriesDataMapper.selectByDeviceCodeAndPointName(deviceCode, pointName);
    }

    @Override
    public List<TimeSeriesData> getByDeviceCodeAndTimeRange(String deviceCode, Date startTime, Date endTime) {
        return timeSeriesDataMapper.selectByDeviceCodeAndTimeRange(deviceCode, startTime, endTime);
    }

    @Override
    public List<TimeSeriesData> getByDeviceCodePointNameAndTimeRange(String deviceCode, String pointName, Date startTime, Date endTime) {
        return timeSeriesDataMapper.selectByDeviceCodePointNameAndTimeRange(deviceCode, pointName, startTime, endTime);
    }

    @Override
    public boolean saveTimeSeriesData(TimeSeriesData timeSeriesData) {
        if (timeSeriesData == null) {
            return false;
        }
        int result = timeSeriesDataMapper.insert(timeSeriesData);
        return result > 0;
    }

    @Override
    public List<String> getPointNamesByDeviceCode(String deviceCode) {
        return timeSeriesDataMapper.selectPointNamesByDeviceCode(deviceCode);
    }

    @Override
    public List<Map<String, Object>> sqlQuery(String sql) {
        return timeSeriesDataMapper.sqlQuery(sql);
    }
}
