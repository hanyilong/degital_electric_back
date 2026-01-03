package com.device.manage.influxdb;

import com.device.manage.entity.TimeSeriesData;
import com.influxdb.v3.client.InfluxDBClient;
import com.influxdb.v3.client.Point;
import com.influxdb.v3.client.write.WritePrecision;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.time.Instant;
import java.util.stream.Stream;

@Service
public class InfluxDBServiceImpl implements InfluxDBService {

    private InfluxDBClient influxDBClient;

    @Value("${spring.influxdb3.host}")
    private String host;

    @Value("${spring.influxdb3.token}")
    private String token;

    @Value("${spring.influxdb3.database}")
    private String database;


    InfluxDBClient getInfluxDBClient() {
        if (influxDBClient == null) {
            char []tokenChars = token.toCharArray();
            influxDBClient = InfluxDBClient.getInstance(host, tokenChars, database);
        }
        return influxDBClient;
    }

    @Override
    public void saveTimeSeriesData(String deviceCode, String modelCode, Map<String, Double> data, Date recordTime) {
        try {
            // 构建表名：T+设备ID
            String measurement = "t_" + deviceCode.toLowerCase();

            System.out.println("准备写入InfluxDB数据：deviceCode=" + deviceCode + ", measurement=" + measurement + ", data=" + data);

            // 使用Point类构建数据点
            Point point = Point.measurement(measurement)
                    .setTag("device_code", deviceCode)
                    .setTag("model_code", modelCode)
                    .setTimestamp(recordTime.getTime(), WritePrecision.MS);

            // 添加所有属性作为字段
            for (Map.Entry<String, Double> entry : data.entrySet()) {
                point.setField(entry.getKey().toLowerCase(), entry.getValue());
                System.out.println("添加字段：" + entry.getKey() + "=" + entry.getValue());
            }

            // 写入数据
            getInfluxDBClient().writePoint(point);
            System.out.println("成功写入数据到InfluxDB");
        } catch (Exception e) {
            System.err.println("Error saving data to InfluxDB: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public List<TimeSeriesData> queryTimeSeriesData(String deviceCode, String pointName, Date startTime, Date endTime) {
        List<TimeSeriesData> result = new ArrayList<>();
        String measurement = "t_" + deviceCode;

        // 将Date转换为ISO8601格式的字符串
        Instant startInstant = startTime.toInstant();
        Instant endInstant = endTime.toInstant();
        String startTimeStr = startInstant.toString();
        String endTimeStr = endInstant.toString();

        // 构建SQL查询语句
        String sqlQuery = String.format("SELECT time, \"%s\" AS value FROM \"%s\" " +
                " WHERE \"device_code\" = '%s' AND time >= '%s' AND time <= '%s' " +
                " ORDER BY time",
                pointName, measurement, deviceCode, startTimeStr, endTimeStr);

        try {
            // 执行查询
            Stream<Object[]> stream = getInfluxDBClient().query(sqlQuery);

            // 处理查询结果
            stream.forEach(row -> {
                TimeSeriesData data = new TimeSeriesData();
                data.setDeviceCode(deviceCode);
                data.setPointName(pointName);

                // row[0] 是纳秒级时间戳, row[1] 是值
                if (row[1] != null) {
                    data.setPointValue(Double.parseDouble(row[1].toString()));
                }
                if (row[0] != null) {
                    // 将纳秒转换为毫秒
                    data.setRecordTime(new Date(((Number) row[0]).longValue() / 1000000));
                }

                result.add(data);
            });
        } catch (Exception e) {
            System.err.println("Error querying data from InfluxDB: " + e.getMessage());
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public Map<String, List<TimeSeriesData>> queryAllPointsData(String deviceCode, Date startTime, Date endTime) {
        Map<String, List<TimeSeriesData>> result = new HashMap<>();
        String measurement = "t_" + deviceCode;

        // 将Date转换为ISO8601格式的字符串
        Instant startInstant = startTime.toInstant();
        Instant endInstant = endTime.toInstant();
        String startTimeStr = startInstant.toString();
        String endTimeStr = endInstant.toString();

        // 首先查询该设备的所有字段名称 - SHOW FIELDS不支持WHERE子句
        String fieldQuery = String.format("SHOW FIELDS FROM \"%s\"", measurement);
        System.out.println("fieldQuery: " + fieldQuery);

        try {
            // 执行查询获取字段名称
            Stream<Object[]> fieldStream = getInfluxDBClient().query(fieldQuery);
            List<String> fieldNames = new ArrayList<>();

            fieldStream.forEach(row -> {
                System.out.println("Field row: " + java.util.Arrays.toString(row));
                System.out.println("Row length: " + row.length);
                // 遍历所有列，查看内容
                for (int i = 0; i < row.length; i++) {
                    System.out.println("Row[" + i + "]: " + row[i] + " (type: " + (row[i] != null ? row[i].getClass().getName() : "null") + ")");
                }
                // SHOW FIELDS查询结果格式：[database, schema, table, field, type, nullable]
                String fieldName = null;
                if (row.length >= 4 && row[3] != null) {
                    fieldName = row[3].toString();
                }
                System.out.println("Trying fieldName: " + fieldName);
                // 排除time列
                if (fieldName != null && !"time".equals(fieldName) && !"deviceCode".equals(fieldName) && !"modelCode".equals(fieldName)) {
                    fieldNames.add(fieldName);
                    System.out.println("Found field: " + fieldName);
                }
            });

            System.out.println("Total fields found: " + fieldNames.size());
            System.out.println("Field names: " + fieldNames);

            // 对每个字段名称执行查询
            for (String fieldName : fieldNames) {
                // 使用标准SQL时间格式，基于传入的时间范围
                String dataQuery = String.format("SELECT time, \"%s\" AS value FROM \"%s\" " +
                        " WHERE \"device_code\" = '%s' AND time >= '%s' AND time <= '%s' " +
                        " ORDER BY time",
                        fieldName, measurement, deviceCode, startTimeStr, endTimeStr);

                System.out.println("dataQuery: " + dataQuery);
                Stream<Object[]> dataStream = getInfluxDBClient().query(dataQuery);

                List<TimeSeriesData> dataList = new ArrayList<>();
                dataStream.forEach(row -> {
                    TimeSeriesData data = new TimeSeriesData();
                    data.setDeviceCode(deviceCode);
                    data.setPointName(fieldName);

                    // row[0] 是时间戳, row[1] 是值
                    if (row[1] != null) {
                        data.setPointValue(Double.parseDouble(row[1].toString()));
                    }
                    if (row[0] != null) {
                        // 将纳秒转换为毫秒
                        data.setRecordTime(new Date(((Number) row[0]).longValue() / 1000000));
                    }

                    dataList.add(data);
                });

                result.put(fieldName, dataList);
            }
        } catch (Exception e) {
            System.err.println("Error querying all points data from InfluxDB: " + e.getMessage());
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public List<Map<String, Object>> sqlQuery(String sql) {
        List<Map<String, Object>> result = new ArrayList<>();

        try {
            System.out.println("Original SQL: " + sql);
            // 修复常见字段名的大小写问题
            String fixedSql = fixCommonFieldNames(sql);
            System.out.println("Fixed SQL: " + fixedSql);

            // 从SQL语句中提取列名
            List<String> columnNames = extractColumnNames(fixedSql);
            System.out.println("Extracted column names: " + columnNames);

            // 检查是否包含通配符
            boolean hasWildcard = columnNames.contains("*");
            System.out.println("Has wildcard: " + hasWildcard);

            // 处理通配符查询：获取实际字段名
            List<String> actualFieldNames = new ArrayList<>();
            if (hasWildcard || fixedSql.toLowerCase().contains("select *") || fixedSql.toLowerCase().matches(".*select.*\\.\\*.*")) {
                // 提取表名
                String tableName = extractTableName(fixedSql);
                System.out.println("Extracted table name: " + tableName);

                if (tableName != null) {
                    // 查询表的所有字段名
                    String showFieldsSql = String.format("SHOW FIELDS FROM \"%s\"", tableName);
                    System.out.println("Show fields SQL: " + showFieldsSql);

                    Stream<Object[]> fieldsStream = getInfluxDBClient().query(showFieldsSql);
                    fieldsStream.forEach(fieldRow -> {
                        // SHOW FIELDS返回的结果格式：[database, schema, table, field, type, nullable]
                        if (fieldRow.length >= 4 && fieldRow[3] != null) {
                            String fieldName = fieldRow[3].toString();
                            actualFieldNames.add(fieldName);
                            System.out.println("Found field: " + fieldName);
                        }
                    });

                    // 添加常用的tag字段
                    actualFieldNames.add("deviceCode");
                    actualFieldNames.add("modelCode");
                    actualFieldNames.add("time");
                }
            }

            // 执行SQL查询
            Stream<Object[]> stream = getInfluxDBClient().query(fixedSql);

            // 处理查询结果
            stream.forEach(row -> {
                Map<String, Object> map = new HashMap<>();

                // 将结果行映射到列名
                for (int i = 0; i < row.length; i++) {
                    String columnName;

                    if ((hasWildcard || fixedSql.toLowerCase().contains("select *") || fixedSql.toLowerCase().matches(".*select.*\\.\\*.*")) && i < actualFieldNames.size()) {
                        // 使用实际字段名
                        columnName = actualFieldNames.get(i);
                    } else if (i < columnNames.size()) {
                        columnName = columnNames.get(i);
                    } else {
                        columnName = "column_" + (i + 1);
                    }

                    Object value = row[i];

                    // 处理时间字段（influxdb返回的时间戳是纳秒级的）
                    if (columnName.equalsIgnoreCase("time")) {
                        if (value instanceof Number) {
                            // 将纳秒转换为毫秒
                            long millis = ((Number) value).longValue() / 1000000;
                            map.put(columnName, millis);
                        } else {
                            map.put(columnName, value);
                        }
                    } else {
                        map.put(columnName, value);
                    }
                }

                result.add(map);
            });
        } catch (Exception e) {
            System.err.println("Error executing SQL query on InfluxDB: " + e.getMessage());
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 从SQL语句中提取列名
     * @param sql SQL语句
     * @return 列名列表
     */
    /**
     * 从SQL语句中提取表名
     * @param sql SQL语句
     * @return 表名
     */
    private String extractTableName(String sql) {
        try {
            // 简化实现：提取FROM和WHERE之间的部分
            int fromIndex = sql.toLowerCase().indexOf("from") + 4;
            int whereIndex = sql.toLowerCase().indexOf("where");
            int endIndex;

            if (whereIndex > fromIndex) {
                endIndex = whereIndex;
            } else {
                // 没有WHERE子句，查找ORDER BY或LIMIT
                int orderIndex = sql.toLowerCase().indexOf("order by");
                int limitIndex = sql.toLowerCase().indexOf("limit");

                if (orderIndex > fromIndex) {
                    endIndex = orderIndex;
                } else if (limitIndex > fromIndex) {
                    endIndex = limitIndex;
                } else {
                    // 没有其他子句，使用字符串末尾
                    endIndex = sql.length();
                }
            }

            String tablePart = sql.substring(fromIndex, endIndex).trim();

            // 去除引号
            tablePart = tablePart.replaceAll("^'|'$|^\"|\"$|^`|`$", "");

            // 处理可能的模式名（如 "public"."table" 或 "database"."schema"."table"）
            if (tablePart.contains(".")) {
                String[] parts = tablePart.split("\\.");
                return parts[parts.length - 1].replaceAll("^'|'$|^\"|\"$|^`|`$", "");
            }

            return tablePart;
        } catch (Exception e) {
            System.err.println("Error extracting table name from SQL: " + e.getMessage());
            return null;
        }
    }

    /**
     * 修复常见字段名的大小写问题
     * @param sql 原始SQL语句
     * @return 修复后的SQL语句
     */
    private String fixCommonFieldNames(String sql) {
        if (sql == null) {
            return null;
        }

        // 修复常见的字段名大小写问题
        // 使用正则表达式确保只替换字段名，不替换表名或其他部分
        // 支持带表名前缀的字段名（如t_1002.devicecode -> t_1002.deviceCode）
        String fixedSql = sql;

        // 设备相关字段 - 先修复大小写
        // 使用更精确的正则表达式，确保能匹配各种大小写组合
        fixedSql = fixedSql.replaceAll("(?i)(\\w+\\.)?devicecode\\b", "$1deviceCode");
        fixedSql = fixedSql.replaceAll("(?i)(\\w+\\.)?modelcode\\b", "$1modelCode");
        fixedSql = fixedSql.replaceAll("(?i)(\\w+\\.)?pointname\\b", "$1pointName");
        fixedSql = fixedSql.replaceAll("(?i)(\\w+\\.)?pointvalue\\b", "$1pointValue");
        fixedSql = fixedSql.replaceAll("(?i)(\\w+\\.)?recordtime\\b", "$1recordTime");

        // 确保所有驼峰命名的字段都带有双引号，防止InfluxDB将其转换为小写

        // 处理带表名前缀的字段（如t_1002.deviceCode -> t_1002."deviceCode"）
        // 注意：InfluxDB需要为带表名前缀的字段添加引号来保持大小写
        // 使用字符替换而非转义，避免双重引号问题
        fixedSql = fixedSql.replaceAll("(\\w+\\.)deviceCode\\b", "$1" + '"' + "deviceCode" + '"');
        fixedSql = fixedSql.replaceAll("(\\w+\\.)modelCode\\b", "$1" + '"' + "modelCode" + '"');
        fixedSql = fixedSql.replaceAll("(\\w+\\.)pointName\\b", "$1" + '"' + "pointName" + '"');
        fixedSql = fixedSql.replaceAll("(\\w+\\.)pointValue\\b", "$1" + '"' + "pointValue" + '"');
        fixedSql = fixedSql.replaceAll("(\\w+\\.)recordTime\\b", "$1" + '"' + "recordTime" + '"');

        // 处理不带表名前缀的字段（如deviceCode -> "deviceCode"）
        fixedSql = fixedSql.replaceAll("(?<!\\w\\.)deviceCode\\b", '"' + "deviceCode" + '"');
        fixedSql = fixedSql.replaceAll("(?<!\\w\\.)modelCode\\b", '"' + "modelCode" + '"');
        fixedSql = fixedSql.replaceAll("(?<!\\w\\.)pointName\\b", '"' + "pointName" + '"');
        fixedSql = fixedSql.replaceAll("(?<!\\w\\.)pointValue\\b", '"' + "pointValue" + '"');
        fixedSql = fixedSql.replaceAll("(?<!\\w\\.)recordTime\\b", '"' + "recordTime" + '"');

        return fixedSql;
    }

    private List<String> extractColumnNames(String sql) {
        List<String> columnNames = new ArrayList<>();

        try {
            // 简化实现：提取SELECT和FROM之间的部分
            int selectIndex = sql.toLowerCase().indexOf("select") + 6;
            int fromIndex = sql.toLowerCase().indexOf("from");

            if (selectIndex > 5 && fromIndex > selectIndex) {
                String columnsPart = sql.substring(selectIndex, fromIndex).trim();

                // 使用更健壮的方式分割列名，处理括号内的逗号
                List<String> columns = new ArrayList<>();
                StringBuilder currentColumn = new StringBuilder();
                int bracketCount = 0;

                for (char c : columnsPart.toCharArray()) {
                    if (c == '(') {
                        bracketCount++;
                        currentColumn.append(c);
                    } else if (c == ')') {
                        bracketCount--;
                        currentColumn.append(c);
                    } else if (c == ',' && bracketCount == 0) {
                        // 只有当括号内外平衡时才分割列名
                        columns.add(currentColumn.toString().trim());
                        currentColumn.setLength(0); // 重置当前列
                    } else {
                        currentColumn.append(c);
                    }
                }

                // 添加最后一个列名
                if (currentColumn.length() > 0) {
                    columns.add(currentColumn.toString().trim());
                }

                for (String column : columns) {
                column = column.trim();

                // 处理带表名前缀的通配符（如 t_1002.*）
                if (column.endsWith(".*")) {
                    // 这是带表名前缀的通配符，直接返回 *
                    columnNames.add("*");
                    continue;
                }

                // 处理函数（先处理函数，因为函数可能包含别名）
                if (column.contains("(")) {
                    // 处理函数，如 COUNT(*) 或 AVG(temperature) AS avg_temp
                    if (column.toLowerCase().contains(" as ")) {
                        // 有别名，使用别名
                        column = column.substring(column.toLowerCase().indexOf(" as ") + 4).trim();
                    } else {
                        // 没有别名，使用函数名
                        int funcIndex = column.indexOf("(");
                        column = column.substring(0, funcIndex).trim();
                    }
                } else {
                    // 处理带别名的列（如 "temperature" AS temp 或 "temperature" temp）
                    if (column.toLowerCase().contains(" as ")) {
                        column = column.substring(column.toLowerCase().indexOf(" as ") + 4).trim();
                    } else if (column.contains(" ")) {
                        // 处理不带AS的别名
                        String[] parts = column.split("\\s+");
                        if (parts.length > 1) {
                            column = parts[parts.length - 1].trim();
                        }
                    }
                }

                // 去除引号
                column = column.replaceAll("^'|'$|^\"|\"$|^`|`$", "");

                // 如果字段名包含表名前缀（如t_1002.deviceCode），只保留最后一部分
                if (column.contains(".")) {
                    String[] parts = column.split("\\.");
                    column = parts[parts.length - 1];
                    // 再次去除引号（如果有）
                    column = column.replaceAll("^'|'$|^\"|\"$|^`|`$", "");
                }

                    columnNames.add(column);
                }
            }
        } catch (Exception e) {
            System.err.println("Error extracting column names from SQL: " + e.getMessage());
            e.printStackTrace();
        }

        return columnNames;
    }
}
