package com.device.manage.mapper;

import com.device.manage.entity.CabinetType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CabinetTypeMapper {
    List<CabinetType> selectAll();
    
    CabinetType selectByCode(@Param("code") String code);
    
    int insert(CabinetType cabinetType);
    
    int update(CabinetType cabinetType);
    
    int deleteByCode(@Param("code") String code);
}