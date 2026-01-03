package com.device.manage.controller;

import com.device.manage.entity.Project;
import com.device.manage.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/project")
@CrossOrigin(origins = "*")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @GetMapping("/all")
    public List<Project> getAllProjects() {
        return projectService.findAll();
    }

    @GetMapping("/{id}")
    public Project getProjectById(@PathVariable Long id) {
        return projectService.findById(id);
    }

    @PostMapping
    public boolean createProject(@RequestBody Project project) {
        return projectService.save(project);
    }

    @PutMapping("/{id}")
    public boolean updateProject(@PathVariable Long id, @RequestBody Project project) {
        project.setId(id);
        return projectService.update(project);
    }

    @DeleteMapping("/{id}")
    public boolean deleteProject(@PathVariable Long id) {
        return projectService.deleteById(id);
    }
}
