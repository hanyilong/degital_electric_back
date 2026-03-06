package com.device.manage.mapper;

import com.device.manage.entity.CabinetRoom;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CabinetRoomMapper {
    List<CabinetRoom> selectAll();
    
    CabinetRoom selectById(@Param("id") Integer id);
    
    int insert(CabinetRoom cabinetRoom);
    
    int update(CabinetRoom cabinetRoom);
    
    int deleteById(@Param("id") Integer id);
}