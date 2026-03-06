package com.device.manage.mapper;

import com.device.manage.entity.AlarmRecord;
import com.device.manage.entity.AlarmStaticByDescription;
import com.device.manage.entity.AlarmStaticByHour;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

@Mapper
public interface  AlarmRecordMapper {
    List<AlarmRecord> selectAll();
    List<AlarmRecord> selectByProjectId(Long projectId);
    AlarmRecord selectById(Long id);
    List<AlarmRecord> selectByDeviceId(String deviceId);
    List<AlarmRecord> selectByDeviceIdAndProjectId(Map<String, Object> params);
    List<AlarmRecord> selectByRuleId(Long ruleId);
    List<AlarmRecord> selectByRuleIdAndProjectId(Map<String, Object> params);
    List<AlarmRecord> selectByStatus(String status);
    List<AlarmRecord> selectByStatusAndProjectId(Map<String, Object> params);
    List<AlarmRecord> selectByConditions(Map<String, Object> conditions);
    int insert(AlarmRecord alarmRecord);
    int updateStatus(@Param("id") Long id, @Param("status") String status, @Param("resolveTime") String resolveTime);
    int deleteById(Long id);

    List<AlarmStaticByDescription> selectAlarmStaticByDescription(Long projectId);

    List<AlarmStaticByHour> selectAlarmStaticByHour(Long projectId);
}
