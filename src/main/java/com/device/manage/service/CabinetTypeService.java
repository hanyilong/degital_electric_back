package com.device.manage.service;

import com.device.manage.entity.CabinetType;

import java.util.List;

public interface CabinetTypeService {
    List<CabinetType> findAll();
    
    CabinetType findByCode(String code);
    
    int create(CabinetType cabinetType);
    
    int update(CabinetType cabinetType);
    
    int delete(String code);
}