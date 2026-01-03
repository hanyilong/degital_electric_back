package com.device.manage.controller;

import com.device.manage.entity.User;
import com.device.manage.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.findById(id);
    }

    @PostMapping
    public boolean createUser(@RequestBody User user) {
        return userService.save(user);
    }

    @PutMapping("/{id}")
    public boolean updateUser(@PathVariable Long id, @RequestBody User user) {
        user.setId(id);
        return userService.update(user);
    }

    @DeleteMapping("/{id}")
    public boolean deleteUser(@PathVariable Long id) {
        return userService.deleteById(id);
    }

    @PostMapping("/login")
    public User login(@RequestBody User loginUser) {
        System.out.println("Login request - Username: " + loginUser.getUsername());
        System.out.println("Login request - Password: " + loginUser.getPassword());
        System.out.println("Login request - ProjectId: " + loginUser.getProjectId());
        System.out.println("Login request - ProjectId type: " + (loginUser.getProjectId() != null ? loginUser.getProjectId().getClass() : "null"));
        
        if (loginUser.getProjectId() != null) {
            User user = userService.login(loginUser.getUsername(), loginUser.getPassword(), loginUser.getProjectId());
            System.out.println("Login result with project: " + user);
            return user;
        } else {
            User user = userService.login(loginUser.getUsername(), loginUser.getPassword());
            System.out.println("Login result without project: " + user);
            return user;
        }
    }
}