package com.device.manage.service.impl;

import com.device.manage.entity.NodeTemplate;
import com.device.manage.mapper.NodeTemplateMapper;
import com.device.manage.service.NodeTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NodeTemplateServiceImpl implements NodeTemplateService {

    @Autowired
    private NodeTemplateMapper nodeTemplateMapper;

    @Override
    public List<NodeTemplate> findAll(Long projectId) {
        return nodeTemplateMapper.selectAll(projectId);
    }

    @Override
    public List<NodeTemplate> findByProjectId(Long projectId) {
        return nodeTemplateMapper.selectByProjectId(projectId);
    }

    @Override
    public NodeTemplate findById(Long id) {
        return nodeTemplateMapper.selectById(id);
    }

    @Override
    public boolean save(NodeTemplate nodeTemplate) {
        return nodeTemplateMapper.insert(nodeTemplate) > 0;
    }

    @Override
    public boolean update(NodeTemplate nodeTemplate) {
        return nodeTemplateMapper.update(nodeTemplate) > 0;
    }

    @Override
    public boolean deleteById(Long id) {
        return nodeTemplateMapper.deleteById(id) > 0;
    }

    @Override
    public List<NodeTemplate> findByFolder(String folderName) {
        return nodeTemplateMapper.selectByFolder(folderName);
    }
}
