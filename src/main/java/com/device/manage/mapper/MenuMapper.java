package com.device.manage.mapper;

import com.device.manage.entity.Menu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MenuMapper {
    // 新增菜单
    int insert(Menu menu);
    
    // 更新菜单
    int update(Menu menu);
    
    // 删除菜单
    int delete(Long id);
    
    // 根据ID查询菜单
    Menu selectById(Long id);
    
    // 查询所有菜单
    List<Menu> selectAll();
    
    // 根据父ID查询菜单
    List<Menu> selectByParentId(Long parentId);
    
    // 根据项目ID查询菜单
    List<Menu> selectByProjectId(Long projectId);
    
    // 根据用户ID查询菜单
    List<Menu> selectByUserId(Long userId);
}