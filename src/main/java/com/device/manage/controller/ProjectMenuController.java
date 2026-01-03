package com.device.manage.controller;

import com.device.manage.entity.ProjectMenu;
import com.device.manage.service.ProjectMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/project-menu")
public class ProjectMenuController {
    
    @Autowired
    private ProjectMenuService projectMenuService;
    
    // 新增项目菜单关联
    @PostMapping
    public ResponseEntity<ProjectMenu> createProjectMenu(@RequestBody ProjectMenu projectMenu) {
        boolean success = projectMenuService.save(projectMenu);
        if (success) {
            return new ResponseEntity<>(projectMenu, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // 批量设置项目菜单
    @PostMapping("/batch")
    public ResponseEntity<Void> batchSetProjectMenus(@RequestParam Long projectId, @RequestBody List<Long> menuIds) {
        boolean success = projectMenuService.saveBatch(projectId, menuIds);
        if (success) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // 删除项目菜单关联
    @DeleteMapping
    public ResponseEntity<Void> deleteProjectMenu(@RequestParam Long projectId, @RequestParam Long menuId) {
        boolean success = projectMenuService.delete(projectId, menuId);
        if (success) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // 根据项目ID查询菜单ID列表
    @GetMapping("/menu-ids/{projectId}")
    public ResponseEntity<List<Long>> getMenuIdsByProjectId(@PathVariable Long projectId) {
        List<Long> menuIds = projectMenuService.listMenuIdsByProjectId(projectId);
        return new ResponseEntity<>(menuIds, HttpStatus.OK);
    }
    
    // 根据项目ID查询项目菜单关联
    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<ProjectMenu>> getProjectMenusByProjectId(@PathVariable Long projectId) {
        List<ProjectMenu> projectMenus = projectMenuService.listByProjectId(projectId);
        return new ResponseEntity<>(projectMenus, HttpStatus.OK);
    }
}