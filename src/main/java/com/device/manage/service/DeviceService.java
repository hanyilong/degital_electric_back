package com.device.manage.service;

import com.device.manage.entity.Device;
import com.device.manage.entity.DeviceStatic;

import java.util.List;

public interface DeviceService {
    List<Device> findAll();
    List<Device> findByProjectId(Long projectId);
    Device findById(Long id);
    Device findByDeviceCode(String deviceId);
    List<Device> findByModelId(Long modelId);
    List<Device> findByModelIdAndProjectId(Long modelId, Long projectId);
    List<Device> searchByKeyword(String keyword);
    List<Device> searchByKeywordAndProjectId(String keyword, Long projectId);
    List<Device> searchByModelIdAndKeyword(Long modelId, String keyword);
    List<Device> findByConditions(java.util.Map<String, Object> params);
    boolean save(Device device);
    boolean update(Device device);
    boolean deleteById(Long id);
    DeviceStatic getDeviceStatic(String projectId);

    void updateDeviceOfflineStatus(List<String> onlineDeviceCodes);
}
