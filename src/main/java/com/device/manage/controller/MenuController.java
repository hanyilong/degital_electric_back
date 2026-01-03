package com.device.manage.controller;

import com.device.manage.entity.Menu;
import com.device.manage.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/menu")
public class MenuController {
    
    @Autowired
    private MenuService menuService;
    
    // 新增菜单
    @PostMapping
    public ResponseEntity<Menu> createMenu(@RequestBody Menu menu) {
        boolean success = menuService.save(menu);
        if (success) {
            return new ResponseEntity<>(menu, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // 更新菜单
    @PutMapping("/{id}")
    public ResponseEntity<Menu> updateMenu(@PathVariable Long id, @RequestBody Menu menu) {
        menu.setId(id);
        boolean success = menuService.update(menu);
        if (success) {
            return new ResponseEntity<>(menu, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // 删除菜单
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMenu(@PathVariable Long id) {
        boolean success = menuService.delete(id);
        if (success) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // 根据ID查询菜单
    @GetMapping("/{id}")
    public ResponseEntity<Menu> getMenuById(@PathVariable Long id) {
        Menu menu = menuService.getById(id);
        if (menu != null) {
            return new ResponseEntity<>(menu, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    // 查询所有菜单
    @GetMapping("/all")
    public ResponseEntity<List<Menu>> getAllMenus() {
        List<Menu> menus = menuService.listAll();
        return new ResponseEntity<>(menus, HttpStatus.OK);
    }
    
    // 查询菜单树
    @GetMapping("/tree")
    public ResponseEntity<List<Menu>> getMenuTree() {
        List<Menu> menus = menuService.listAll();
        List<Menu> menuTree = menuService.buildMenuTree(menus);
        return new ResponseEntity<>(menuTree, HttpStatus.OK);
    }
    
    // 根据项目ID查询菜单
    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<Menu>> getMenusByProjectId(@PathVariable Long projectId) {
        List<Menu> menus = menuService.listByProjectId(projectId);
        List<Menu> menuTree = menuService.buildMenuTree(menus);
        return new ResponseEntity<>(menuTree, HttpStatus.OK);
    }
}