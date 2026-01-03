package com.device.manage.service;

import com.device.manage.entity.ThingModel;
import java.util.List;

public interface ThingModelService {
    List<ThingModel> findAll(Long projectId);
    List<ThingModel> findByProjectId(Long projectId);
    ThingModel findById(Long id);
    boolean save(ThingModel thingModel);
    boolean update(ThingModel thingModel);
    boolean deleteById(Long id);
    boolean fixJsonData();
}