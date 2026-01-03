package com.device.manage.service;

import com.device.manage.entity.NodeTemplate;
import java.util.List;

public interface NodeTemplateService {
    List<NodeTemplate> findAll(Long projectId);
    List<NodeTemplate> findByProjectId(Long projectId);
    NodeTemplate findById(Long id);
    boolean save(NodeTemplate nodeTemplate);
    boolean update(NodeTemplate nodeTemplate);
    boolean deleteById(Long id);
    List<NodeTemplate> findByFolder(String folderName);
}
