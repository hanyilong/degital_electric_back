package com.device.manage.mapper;

import com.device.manage.entity.ProjectMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProjectMenuMapper {
    // 新增项目菜单关联
    int insert(ProjectMenu projectMenu);
    
    // 删除项目菜单关联
    int delete(@Param("projectId") Long projectId, @Param("menuId") Long menuId);
    
    // 根据项目ID删除所有关联
    int deleteByProjectId(Long projectId);
    
    // 根据菜单ID删除所有关联
    int deleteByMenuId(Long menuId);
    
    // 根据项目ID查询所有关联的菜单ID
    List<Long> selectMenuIdsByProjectId(Long projectId);
    
    // 根据项目ID查询所有关联的菜单
    List<ProjectMenu> selectByProjectId(Long projectId);
    
    // 根据菜单ID查询所有关联的项目
    List<ProjectMenu> selectByMenuId(Long menuId);
}