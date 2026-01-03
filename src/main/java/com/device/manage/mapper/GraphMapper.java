package com.device.manage.mapper;

import com.device.manage.entity.Graph;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface GraphMapper {
    List<Graph> selectAll(@Param("projectId") Long projectId);
    List<Graph> selectByProjectId(Long projectId);
    List<Graph> selectByProjectIdAndName(@Param("projectId") Long projectId, @Param("name") String name);
    Graph selectById(Long id);
    int insert(Graph graph);
    int update(Graph graph);
    int deleteById(Long id);
}
