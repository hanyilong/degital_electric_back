package com.device.manage.controller;

import com.device.manage.entity.NodeTemplate;
import com.device.manage.service.NodeTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/node-template")
@CrossOrigin(origins = "*")
public class NodeTemplateController {

    @Autowired
    private NodeTemplateService nodeTemplateService;

    @GetMapping("/listByFolder")
    public List<NodeTemplate> getNodeTemplatesByFolder(@RequestParam(required = false) String folderName) {
        return nodeTemplateService.findByFolder(folderName);
    }

    @GetMapping("/{id}")
    public NodeTemplate getNodeTemplateById(@PathVariable Long id) {
        return nodeTemplateService.findById(id);
    }

    @PostMapping
    public boolean createNodeTemplate(@RequestBody NodeTemplate nodeTemplate) {
        return nodeTemplateService.save(nodeTemplate);
    }

    @PostMapping("/{id}")
    public boolean updateNodeTemplate(@PathVariable Long id, @RequestBody NodeTemplate nodeTemplate) {
        nodeTemplate.setId(id);
        return nodeTemplateService.update(nodeTemplate);
    }

    @DeleteMapping("/{id}")
    public boolean deleteNodeTemplate(@PathVariable Long id) {
        return nodeTemplateService.deleteById(id);
    }
}
