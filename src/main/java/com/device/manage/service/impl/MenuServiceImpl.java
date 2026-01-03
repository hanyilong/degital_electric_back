package com.device.manage.service.impl;

import com.device.manage.entity.Menu;
import com.device.manage.mapper.MenuMapper;
import com.device.manage.mapper.ProjectMenuMapper;
import com.device.manage.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuServiceImpl implements MenuService {
    
    @Autowired
    private MenuMapper menuMapper;
    
    @Autowired
    private ProjectMenuMapper projectMenuMapper;

    @Override
    public boolean save(Menu menu) {
        return menuMapper.insert(menu) > 0;
    }

    @Override
    public boolean update(Menu menu) {
        return menuMapper.update(menu) > 0;
    }

    @Override
    public boolean delete(Long id) {
        // 先删除项目菜单关联
        projectMenuMapper.deleteByMenuId(id);
        // 再删除菜单
        return menuMapper.delete(id) > 0;
    }

    @Override
    public Menu getById(Long id) {
        return menuMapper.selectById(id);
    }

    @Override
    public List<Menu> listAll() {
        return menuMapper.selectAll();
    }

    @Override
    public List<Menu> buildMenuTree(List<Menu> menus) {
        List<Menu> tree = new ArrayList<>();
        
        // 先找出所有根节点
        List<Menu> rootMenus = menus.stream()
                .filter(menu -> menu.getParentId() == null || menu.getParentId() == 0)
                .collect(Collectors.toList());
        
        // 为每个根节点构建子菜单
        for (Menu rootMenu : rootMenus) {
            rootMenu.setChildren(findChildren(menus, rootMenu.getId()));
            tree.add(rootMenu);
        }
        
        return tree;
    }
    
    // 递归查找子菜单
    private List<Menu> findChildren(List<Menu> menus, Long parentId) {
        List<Menu> children = new ArrayList<>();
        
        for (Menu menu : menus) {
            if (menu.getParentId() != null && menu.getParentId().equals(parentId)) {
                menu.setChildren(findChildren(menus, menu.getId()));
                children.add(menu);
            }
        }
        
        return children;
    }

    @Override
    public List<Menu> listByProjectId(Long projectId) {
        return menuMapper.selectByProjectId(projectId);
    }

    @Override
    public List<Menu> listByUserId(Long userId) {
        return menuMapper.selectByUserId(userId);
    }
}