package com.device.manage.mapper;

import com.device.manage.entity.AlarmRule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

@Mapper
public interface AlarmRuleMapper {
    List<AlarmRule> selectAll();
    List<AlarmRule> selectByProjectId(Long projectId);
    AlarmRule selectById(Long id);
    List<AlarmRule> selectByModelId(Long modelId);
    List<AlarmRule> selectByModelIdAndProjectId(Map<String, Object> params);
    List<AlarmRule> selectByDeviceCode(String deviceCode);
    List<AlarmRule> selectByDeviceCodeAndProjectId(Map<String, Object> params);
    int insert(AlarmRule alarmRule);
    int update(AlarmRule alarmRule);
    int deleteById(Long id);
    int updateStatus(@Param("id") Long id, @Param("isActive") Boolean isActive);
}
