package com.device.manage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.device.manage.mapper")
@EnableScheduling
public class DeviceManageApplication {
    public static void main(String[] args) {
        SpringApplication.run(DeviceManageApplication.class, args);
    }
}