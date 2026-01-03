package com.device.manage.controller;

import com.device.manage.entity.ThingModel;
import com.device.manage.service.ThingModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/thing-model")
@CrossOrigin(origins = "*")
public class ThingModelController {

    @Autowired
    private ThingModelService thingModelService;

    @GetMapping
    public List<ThingModel> getAllThingModels(@RequestParam(required = false) Long projectId) {
        return thingModelService.findAll(projectId);
    }

    @GetMapping("/{id}")
    public ThingModel getThingModelById(@PathVariable Long id) {
        return thingModelService.findById(id);
    }

    @PostMapping
    public boolean createThingModel(@RequestBody ThingModel thingModel) {
        return thingModelService.save(thingModel);
    }

    @PutMapping("/{id}")
    public boolean updateThingModel(@PathVariable Long id, @RequestBody ThingModel thingModel) {
        thingModel.setId(id);
        return thingModelService.update(thingModel);
    }

    @DeleteMapping("/{id}")
    public boolean deleteThingModel(@PathVariable Long id) {
        return thingModelService.deleteById(id);
    }
    
    @PostMapping("/fix-json")
    public boolean fixJsonData() {
        return thingModelService.fixJsonData();
    }
}