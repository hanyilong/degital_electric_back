package com.device.manage.service.impl;

import com.device.manage.entity.CabinetRoom;
import com.device.manage.mapper.CabinetRoomMapper;
import com.device.manage.service.CabinetRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CabinetRoomServiceImpl implements CabinetRoomService {
    
    @Autowired
    private CabinetRoomMapper cabinetRoomMapper;
    
    @Override
    public List<CabinetRoom> findAll() {
        return cabinetRoomMapper.selectAll();
    }
    
    @Override
    public CabinetRoom findById(Integer id) {
        return cabinetRoomMapper.selectById(id);
    }
    
    @Override
    public int create(CabinetRoom cabinetRoom) {
        return cabinetRoomMapper.insert(cabinetRoom);
    }
    
    @Override
    public int update(CabinetRoom cabinetRoom) {
        return cabinetRoomMapper.update(cabinetRoom);
    }
    
    @Override
    public int delete(Integer id) {
        return cabinetRoomMapper.deleteById(id);
    }
}