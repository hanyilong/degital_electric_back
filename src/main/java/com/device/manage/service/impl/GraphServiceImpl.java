package com.device.manage.service.impl;

import com.device.manage.entity.Graph;
import com.device.manage.mapper.GraphMapper;
import com.device.manage.service.GraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GraphServiceImpl implements GraphService {

    @Autowired
    private GraphMapper graphMapper;

    @Override
    public List<Graph> findAll(Long projectId) {
        return graphMapper.selectAll(projectId);
    }

    @Override
    public List<Graph> findByProjectId(Long projectId) {
        return graphMapper.selectByProjectId(projectId);
    }

    @Override
    public List<Graph> findByProjectIdAndName(Long projectId, String name) {
        return graphMapper.selectByProjectIdAndName(projectId, name);
    }
    @Override
    public Graph findById(Long id) {
        return graphMapper.selectById(id);
    }

    @Override
    public boolean save(Graph graph) {
        return graphMapper.insert(graph) > 0;
    }

    @Override
    public boolean update(Graph graph) {
        return graphMapper.update(graph) > 0;
    }

    @Override
    public boolean deleteById(Long id) {
        return graphMapper.deleteById(id) > 0;
    }
}
