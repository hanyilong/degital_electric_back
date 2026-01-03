package com.device.manage.util;

import java.text.SimpleDateFormat;
import java.util.*;

public class QueryResultConverter {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    /**
     * 将查询结果转换为field-data格式
     * @param results 查询结果
     * @return 转换后的结果
     */
    public static Map<String, Object> convertToFieldDataFormat(List<Map<String, Object>> results) {
        Map<String, Object> result = new HashMap<>();
        
        if (results == null || results.isEmpty()) {
            result.put("field", new ArrayList<>());
            result.put("data", new ArrayList<>());
            return result;
        }
        
        // 获取所有字段名
        Set<String> fieldNames = results.get(0).keySet();
        List<String> fields = new ArrayList<>(fieldNames);
        result.put("field", fields);
        
        // 转换数据行
        List<List<Object>> data = new ArrayList<>();
        for (Map<String, Object> row : results) {
            List<Object> dataRow = new ArrayList<>();
            for (String field : fields) {
                Object value = row.get(field);
                // 格式化时间字段
                if (value instanceof Date) {
                    dataRow.add(DATE_FORMAT.format((Date) value));
                } else {
                    dataRow.add(value);
                }
            }
            data.add(dataRow);
        }
        
        result.put("data", data);
        return result;
    }
}