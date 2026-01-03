package com.device.manage.service;

import com.device.manage.entity.Project;
import java.util.List;

public interface ProjectService {
    List<Project> findAll();
    Project findById(Long id);
    Project findByProjectCode(String projectCode);
    boolean save(Project project);
    boolean update(Project project);
    boolean deleteById(Long id);
}