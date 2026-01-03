package com.device.manage.service;

import com.device.manage.entity.Graph;
import java.util.List;

public interface GraphService {
    List<Graph> findAll(Long projectId);
    List<Graph> findByProjectId(Long projectId);
    Graph findById(Long id);
    List<Graph> findByProjectIdAndName(Long projectId, String name);
    boolean save(Graph graph);
    boolean update(Graph graph);
    boolean deleteById(Long id);
}
