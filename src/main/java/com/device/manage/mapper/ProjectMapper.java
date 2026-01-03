package com.device.manage.mapper;

import com.device.manage.entity.Project;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface ProjectMapper {
    List<Project> selectAll();
    Project selectById(Long id);
    Project selectByProjectCode(String projectCode);
    int insert(Project project);
    int update(Project project);
    int deleteById(Long id);
}