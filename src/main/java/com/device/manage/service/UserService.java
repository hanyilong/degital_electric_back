package com.device.manage.service;

import com.device.manage.entity.User;
import java.util.List;

public interface UserService {
    List<User> findAll();
    User findById(Long id);
    User findByUsername(String username);
    User login(String username, String password);
    User login(String username, String password, Long projectId);
    boolean save(User user);
    boolean update(User user);
    boolean deleteById(Long id);
}