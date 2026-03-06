package com.device.manage.service.impl;

import com.device.manage.entity.Device;
import com.device.manage.entity.DeviceStatic;
import com.device.manage.mapper.DeviceMapper;
import com.device.manage.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeviceServiceImpl implements DeviceService {

    @Autowired
    private DeviceMapper deviceMapper;

    @Override
    public List<Device> findAll() {
        return deviceMapper.selectAll();
    }

    @Override
    public List<Device> findByProjectId(Long projectId) {
        return deviceMapper.selectByProjectId(projectId);
    }

    @Override
    public Device findById(Long id) {
        return deviceMapper.selectById(id);
    }

    @Override
    public Device findByDeviceCode(String deviceCode) {
        return deviceMapper.selectByDeviceCode(deviceCode);
    }

    @Override
    public List<Device> findByModelId(Long modelId) {
        return deviceMapper.selectByModelId(modelId);
    }

    @Override
    public List<Device> findByModelIdAndProjectId(Long modelId, Long projectId) {
        java.util.Map<String, Object> params = new java.util.HashMap<>();
        params.put("modelId", modelId);
        params.put("projectId", projectId);
        return deviceMapper.selectByModelIdAndProjectId(params);
    }

    @Override
    public boolean save(Device device) {
        return deviceMapper.insert(device) > 0;
    }

    @Override
    public boolean update(Device device) {
        return deviceMapper.update(device) > 0;
    }

    @Override
    public boolean deleteById(Long id) {
        return deviceMapper.deleteById(id) > 0;
    }

    @Override
    public DeviceStatic getDeviceStatic(String projectId) {
        java.util.Map<String, Object> params = new java.util.HashMap<>();
        params.put("projectId", projectId);
        return deviceMapper.selectDeviceStatic(params);
    }

    @Override
    public void updateDeviceOfflineStatus(List<String> onlineDeviceCodes) {
        deviceMapper.updateDeviceStatusOffline(onlineDeviceCodes);
    }

    @Override
    public List<Device> searchByKeyword(String keyword) {
        return deviceMapper.selectByKeyword(keyword);
    }

    @Override
    public List<Device> searchByKeywordAndProjectId(String keyword, Long projectId) {
        java.util.Map<String, Object> params = new java.util.HashMap<>();
        params.put("keyword", keyword);
        params.put("projectId", projectId);
        return deviceMapper.selectByKeywordAndProjectId(params);
    }

    @Override
    public List<Device> searchByModelIdAndKeyword(Long modelId, String keyword) {
        java.util.Map<String, Object> params = new java.util.HashMap<>();
        params.put("modelId", modelId);
        params.put("keyword", keyword);
        return deviceMapper.selectByModelIdAndKeyword(params);
    }

    @Override
    public List<Device> findByConditions(java.util.Map<String, Object> params) {
        return deviceMapper.selectByConditions(params);
    }
}
