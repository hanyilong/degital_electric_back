package com.device.manage.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        // 执行初始化脚本列表
        String[] sqlFiles = {"db/init.sql", "db/create_time_series_table.sql"};

        for (String sqlFile : sqlFiles) {
            ClassPathResource resource = new ClassPathResource(sqlFile);
            if (resource.exists()) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
                    // 读取所有行并过滤掉注释和空行
                    String content = reader.lines()
                            .map(line -> line.replaceFirst("--.*$", "").trim()) // 移除行注释并修剪空白
                            .filter(line -> !line.isEmpty()) // 过滤掉空行
                            .collect(Collectors.joining("\n"));

                    // 按分号拆分SQL语句
                    String[] sqlStatements = content.split(";");

                    // 逐条执行SQL语句
                    for (String sql : sqlStatements) {
                        String trimmedSql = sql.trim();
                        if (!trimmedSql.isEmpty()) {
                            jdbcTemplate.execute(trimmedSql);
                        }
                    }

                    System.out.println(sqlFile + " 执行成功");
                } catch (Exception e) {
                    System.err.println("执行" + sqlFile + "失败: " + e.getMessage());
                    e.printStackTrace();
                }
            } else {
                System.err.println(sqlFile + " 文件不存在");
            }
        }
    }
}
