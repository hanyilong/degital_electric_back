package com.device.manage.service;

import com.device.manage.entity.CabinetRoom;

import java.util.List;

public interface CabinetRoomService {
    List<CabinetRoom> findAll();
    
    CabinetRoom findById(Integer id);
    
    int create(CabinetRoom cabinetRoom);
    
    int update(CabinetRoom cabinetRoom);
    
    int delete(Integer id);
}