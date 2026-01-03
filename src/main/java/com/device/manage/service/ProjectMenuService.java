package com.device.manage.service;

import com.device.manage.entity.ProjectMenu;

import java.util.List;

public interface ProjectMenuService {
    // 新增项目菜单关联
    boolean save(ProjectMenu projectMenu);
    
    // 批量新增项目菜单关联
    boolean saveBatch(Long projectId, List<Long> menuIds);
    
    // 删除项目菜单关联
    boolean delete(Long projectId, Long menuId);
    
    // 根据项目ID删除所有关联
    boolean deleteByProjectId(Long projectId);
    
    // 根据项目ID查询所有关联的菜单ID
    List<Long> listMenuIdsByProjectId(Long projectId);
    
    // 根据项目ID查询所有关联的菜单
    List<ProjectMenu> listByProjectId(Long projectId);
}