package com.device.manage.mapper;

import com.device.manage.entity.NodeTemplate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface NodeTemplateMapper {
    List<NodeTemplate> selectAll(@Param("projectId") Long projectId);
    List<NodeTemplate> selectByProjectId(Long projectId);
    NodeTemplate selectById(Long id);
    int insert(NodeTemplate nodeTemplate);
    int update(NodeTemplate nodeTemplate);
    int deleteById(Long id);
    List<NodeTemplate> selectByFolder(@Param("folderName") String folderName);
}
