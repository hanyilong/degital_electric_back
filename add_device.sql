USE device_manage;

-- 仅添加device001设备，不影响其他现有设备
INSERT IGNORE INTO device (device_name, device_id, device_type, device_status, model_id, device_info) VALUES
('测试设备', 'device001', 'sensor', 'online', 1, '{"location": "测试环境", "manufacturer": "TestTech"}');
