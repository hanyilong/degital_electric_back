package com.device.manage.mapper;

import com.device.manage.entity.ThingModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface ThingModelMapper {
    List<ThingModel> selectAll(@Param("projectId") Long projectId);
    List<ThingModel> selectByProjectId(Long projectId);
    ThingModel selectById(Long id);
    int insert(ThingModel thingModel);
    int update(ThingModel thingModel);
    int deleteById(Long id);
}