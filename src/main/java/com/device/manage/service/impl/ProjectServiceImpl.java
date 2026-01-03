package com.device.manage.service.impl;

import com.device.manage.entity.Project;
import com.device.manage.mapper.ProjectMapper;
import com.device.manage.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectMapper projectMapper;

    @Override
    public List<Project> findAll() {
        return projectMapper.selectAll();
    }

    @Override
    public Project findById(Long id) {
        return projectMapper.selectById(id);
    }

    @Override
    public Project findByProjectCode(String projectCode) {
        return projectMapper.selectByProjectCode(projectCode);
    }

    @Override
    public boolean save(Project project) {
        return projectMapper.insert(project) > 0;
    }

    @Override
    public boolean update(Project project) {
        return projectMapper.update(project) > 0;
    }

    @Override
    public boolean deleteById(Long id) {
        return projectMapper.deleteById(id) > 0;
    }
}