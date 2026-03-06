package com.device.manage.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 定时任务服务类
 */
@Service
public class ScheduledTaskService {

    @Autowired
    TimeSeriesDataService timeSeriesDataService;
    @Autowired
    DeviceService deviceService;

    private static final Logger logger = LoggerFactory.getLogger(ScheduledTaskService.class);

    /**
     * 每小时执行一次的定时任务
     * cron表达式：0 * * * * ? 表示每小时的第0分0秒执行
     */
    @Scheduled(cron = "0 * * * * ?")
    public void hourlyTask() {
        logger.info("执行每小时定时任务开始");
        List<String> onlineDeviceCodes = timeSeriesDataService.findOfflineDeviceCodes();
        deviceService.updateDeviceOfflineStatus(onlineDeviceCodes);

        logger.info("执行每小时定时任务结束");
    }
}
