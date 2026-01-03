package com.device.manage.service.impl;

import com.device.manage.entity.ThingModel;
import com.device.manage.mapper.ThingModelMapper;
import com.device.manage.service.ThingModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ThingModelServiceImpl implements ThingModelService {

    @Autowired
    private ThingModelMapper thingModelMapper;

    @Override
    public List<ThingModel> findAll(Long projectId) {
        return thingModelMapper.selectAll(projectId);
    }

    @Override
    public List<ThingModel> findByProjectId(Long projectId) {
        return thingModelMapper.selectByProjectId(projectId);
    }

    @Override
    public ThingModel findById(Long id) {
        return thingModelMapper.selectById(id);
    }

    @Override
    public boolean save(ThingModel thingModel) {
        return thingModelMapper.insert(thingModel) > 0;
    }

    @Override
    public boolean update(ThingModel thingModel) {
        return thingModelMapper.update(thingModel) > 0;
    }

    @Override
    public boolean deleteById(Long id) {
        return thingModelMapper.deleteById(id) > 0;
    }

    @Override
    public boolean fixJsonData() {
        try {
            // 查询所有物模型数据
            List<ThingModel> thingModels = thingModelMapper.selectAll(null);
            
            for (ThingModel model : thingModels) {
                String functions = model.getFunctions();
                String events = model.getEvents();
                boolean needUpdate = false;

                // 修复functions字段
                if (functions != null && !functions.isEmpty()) {
                    String fixedFunctions = fixJsonString(functions);
                    if (!fixedFunctions.equals(functions)) {
                        model.setFunctions(fixedFunctions);
                        needUpdate = true;
                    }
                }

                // 修复events字段
                if (events != null && !events.isEmpty()) {
                    String fixedEvents = fixJsonString(events);
                    if (!fixedEvents.equals(events)) {
                        model.setEvents(fixedEvents);
                        needUpdate = true;
                    }
                }

                // 更新数据
                if (needUpdate) {
                    thingModelMapper.update(model);
                }
            }
            
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 修复JSON字符串中的嵌套引号问题
    private String fixJsonString(String json) {
        if (json == null || json.isEmpty()) {
            return json;
        }
        
        // 修复嵌套JSON中的引号问题
        // 1. 先找到所有的嵌套JSON结构
        // 2. 将内部JSON结构中的引号进行转义
        StringBuilder fixed = new StringBuilder();
        int depth = 0;
        boolean inString = false;
        
        for (int i = 0; i < json.length(); i++) {
            char c = json.charAt(i);
            
            // 处理字符串中的引号
            if (c == '"') {
                inString = !inString;
                fixed.append(c);
            } 
            // 处理对象的开始和结束
            else if (c == '{') {
                if (inString) {
                    // 在字符串内部，需要转义
                    fixed.append('\\').append(c);
                } else {
                    fixed.append(c);
                    depth++;
                }
            } 
            else if (c == '}') {
                if (inString) {
                    // 在字符串内部，需要转义
                    fixed.append('\\').append(c);
                } else {
                    fixed.append(c);
                    depth--;
                }
            } 
            else {
                fixed.append(c);
            }
        }
        
        return fixed.toString();
    }
}