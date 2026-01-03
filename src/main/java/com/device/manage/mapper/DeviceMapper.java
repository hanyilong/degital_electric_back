package com.device.manage.mapper;

import com.device.manage.entity.Device;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface DeviceMapper {
    List<Device> selectAll();
    List<Device> selectByProjectId(Long projectId);
    Device selectById(Long id);
    Device selectByDeviceCode(String deviceCode);
    List<Device> selectByModelId(Long modelId);
    List<Device> selectByModelIdAndProjectId(java.util.Map<String, Object> params);
    List<Device> selectByKeyword(String keyword);
    List<Device> selectByKeywordAndProjectId(java.util.Map<String, Object> params);
    List<Device> selectByModelIdAndKeyword(java.util.Map<String, Object> params);
    List<Device> selectByConditions(java.util.Map<String, Object> params);
    int insert(Device device);
    int update(Device device);
    int deleteById(Long id);
}
