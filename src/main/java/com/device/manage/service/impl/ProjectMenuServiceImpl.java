package com.device.manage.service.impl;

import com.device.manage.entity.ProjectMenu;
import com.device.manage.mapper.ProjectMenuMapper;
import com.device.manage.service.ProjectMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ProjectMenuServiceImpl implements ProjectMenuService {
    
    @Autowired
    private ProjectMenuMapper projectMenuMapper;

    @Override
    public boolean save(ProjectMenu projectMenu) {
        projectMenu.setCreateTime(new Date());
        projectMenu.setUpdateTime(new Date());
        return projectMenuMapper.insert(projectMenu) > 0;
    }

    @Override
    public boolean saveBatch(Long projectId, List<Long> menuIds) {
        // 先删除该项目已有的所有菜单关联
        projectMenuMapper.deleteByProjectId(projectId);
        
        // 批量插入新的关联
        for (Long menuId : menuIds) {
            ProjectMenu projectMenu = new ProjectMenu();
            projectMenu.setProjectId(projectId);
            projectMenu.setMenuId(menuId);
            projectMenu.setCreateTime(new Date());
            projectMenu.setUpdateTime(new Date());
            projectMenuMapper.insert(projectMenu);
        }
        
        return true;
    }

    @Override
    public boolean delete(Long projectId, Long menuId) {
        return projectMenuMapper.delete(projectId, menuId) > 0;
    }

    @Override
    public boolean deleteByProjectId(Long projectId) {
        return projectMenuMapper.deleteByProjectId(projectId) > 0;
    }

    @Override
    public List<Long> listMenuIdsByProjectId(Long projectId) {
        return projectMenuMapper.selectMenuIdsByProjectId(projectId);
    }

    @Override
    public List<ProjectMenu> listByProjectId(Long projectId) {
        return projectMenuMapper.selectByProjectId(projectId);
    }
}