package com.device.manage.influxdb;

import com.influxdb.v3.client.InfluxDBClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InfluxDBConfig {

    @Value("${spring.influxdb3.host}")
    private String influxDBHost;

    @Value("${spring.influxdb3.database}")
    private String influxDBDatabase;

    @Value("${spring.influxdb3.token}")
    private String influxDBToken;

    @Bean
    public InfluxDBClient influxDBClient() {
        // 构建InfluxDB URL
        String influxDBUrl = "http://" + influxDBHost;
        
        return InfluxDBClient.getInstance(
                influxDBUrl,
                influxDBToken.toCharArray(),
                influxDBDatabase
        );
    }
}
