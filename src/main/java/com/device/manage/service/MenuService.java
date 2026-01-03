package com.device.manage.service;

import com.device.manage.entity.Menu;

import java.util.List;

public interface MenuService {
    // 新增菜单
    boolean save(Menu menu);
    
    // 更新菜单
    boolean update(Menu menu);
    
    // 删除菜单
    boolean delete(Long id);
    
    // 根据ID查询菜单
    Menu getById(Long id);
    
    // 查询所有菜单
    List<Menu> listAll();
    
    // 构建菜单树
    List<Menu> buildMenuTree(List<Menu> menus);
    
    // 根据项目ID查询菜单
    List<Menu> listByProjectId(Long projectId);
    
    // 根据用户ID查询菜单
    List<Menu> listByUserId(Long userId);
}