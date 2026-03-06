package com.device.manage.service.impl;

import com.device.manage.entity.CabinetType;
import com.device.manage.mapper.CabinetTypeMapper;
import com.device.manage.service.CabinetTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CabinetTypeServiceImpl implements CabinetTypeService {
    
    @Autowired
    private CabinetTypeMapper cabinetTypeMapper;
    
    @Override
    public List<CabinetType> findAll() {
        return cabinetTypeMapper.selectAll();
    }
    
    @Override
    public CabinetType findByCode(String code) {
        return cabinetTypeMapper.selectByCode(code);
    }
    
    @Override
    public int create(CabinetType cabinetType) {
        return cabinetTypeMapper.insert(cabinetType);
    }
    
    @Override
    public int update(CabinetType cabinetType) {
        return cabinetTypeMapper.update(cabinetType);
    }
    
    @Override
    public int delete(String code) {
        return cabinetTypeMapper.deleteByCode(code);
    }
}