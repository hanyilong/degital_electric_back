package com.device.manage.controller;

import com.device.manage.entity.Device;
import com.device.manage.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/device")
@CrossOrigin(origins = "*")
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    @GetMapping
    public List<Device> getAllDevices(@RequestParam(required = false) String projectId) {
        if (projectId != null && !projectId.isEmpty()) {
            try {
                Long id = Long.parseLong(projectId);
                return deviceService.findByProjectId(id);
            } catch (NumberFormatException e) {
                // 如果projectId不是有效的数字，返回所有设备
                return deviceService.findAll();
            }
        }
        return deviceService.findAll();
    }

    @GetMapping("/{id}")
    public Device getDeviceById(@PathVariable Long id) {
        return deviceService.findById(id);
    }

    @GetMapping("/model/{modelId}")
    public List<Device> getDevicesByModelId(@PathVariable Long modelId, @RequestParam(required = false) String projectId) {
        if (projectId != null && !projectId.isEmpty()) {
            try {
                Long id = Long.parseLong(projectId);
                List<Device> rlt =  deviceService.findByModelIdAndProjectId(modelId, id);
                return rlt;
            } catch (NumberFormatException e) {
                // 如果projectId不是有效的数字，只按modelId查询
                return deviceService.findByModelId(modelId);
            }
        }
        return deviceService.findByModelId(modelId);
    }

    @PostMapping
    public boolean createDevice(@RequestBody Device device) {
        return deviceService.save(device);
    }

    @PutMapping("/{id}")
    public boolean updateDevice(@PathVariable Long id, @RequestBody Device device) {
        device.setId(id);
        return deviceService.update(device);
    }

    @DeleteMapping("/{id}")
    public boolean deleteDevice(@PathVariable Long id) {
        return deviceService.deleteById(id);
    }

    @GetMapping("/search")
    public List<Device> searchDevices(@RequestParam String keyword, @RequestParam(required = false) String projectId) {
        if (projectId != null && !projectId.isEmpty()) {
            try {
                Long id = Long.parseLong(projectId);
                return deviceService.searchByKeywordAndProjectId(keyword, id);
            } catch (NumberFormatException e) {
                // 如果projectId不是有效的数字，只按keyword查询
                return deviceService.searchByKeyword(keyword);
            }
        }
        return deviceService.searchByKeyword(keyword);
    }

    @GetMapping("/model/{modelId}/search")
    public List<Device> searchDevicesByModelIdAndKeyword(@PathVariable Long modelId, @RequestParam String keyword, @RequestParam(required = false) String projectId) {
        // 这里可以扩展支持projectId
        if (projectId != null && !projectId.isEmpty()) {
            try {
                Long id = Long.parseLong(projectId);
                // 如果service支持按modelId、keyword和projectId查询，则调用相应方法
                // 目前先保持原功能
                return deviceService.searchByModelIdAndKeyword(modelId, keyword);
            } catch (NumberFormatException e) {
                // 如果projectId不是有效的数字，只按modelId和keyword查询
                return deviceService.searchByModelIdAndKeyword(modelId, keyword);
            }
        }
        return deviceService.searchByModelIdAndKeyword(modelId, keyword);
    }

    @GetMapping("/search/conditions")
    public List<Device> searchDevicesByConditions(
            @RequestParam(required = false) String deviceType,
            @RequestParam(required = false) String deviceId,
            @RequestParam(required = false) String deviceName,
            @RequestParam(required = false) String deviceStatus,
            @RequestParam(required = false) String projectId) {
        java.util.Map<String, Object> params = new java.util.HashMap<>();
        params.put("deviceType", deviceType);
        params.put("deviceId", deviceId);
        params.put("deviceName", deviceName);
        params.put("deviceStatus", deviceStatus);

        if (projectId != null && !projectId.isEmpty()) {
            try {
                Long id = Long.parseLong(projectId);
                params.put("projectId", id);
            } catch (NumberFormatException e) {
                // 如果projectId不是有效的数字，忽略projectId参数
            }
        }

        return deviceService.findByConditions(params);
    }
}
