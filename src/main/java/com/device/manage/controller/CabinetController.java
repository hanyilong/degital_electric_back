package com.device.manage.controller;

import com.device.manage.entity.CabinetRoom;
import com.device.manage.entity.CabinetType;
import com.device.manage.service.CabinetRoomService;
import com.device.manage.service.CabinetTypeService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cabinet")
public class CabinetController {

    @Autowired
    private CabinetTypeService cabinetTypeService;

    @Autowired
    private CabinetRoomService cabinetRoomService;

    // CabinetType 相关接口

    @GetMapping("/type")
    public ResponseEntity<List<CabinetType>> getAllCabinetTypes() {
        List<CabinetType> cabinetTypes = cabinetTypeService.findAll();
        return new ResponseEntity<>(cabinetTypes, HttpStatus.OK);
    }

    @GetMapping("/type/{code}")
    public ResponseEntity<CabinetType> getCabinetTypeByCode(@PathVariable("code") String code) {
        CabinetType cabinetType = cabinetTypeService.findByCode(code);
        if (cabinetType != null) {
            return new ResponseEntity<>(cabinetType, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/type")
    public ResponseEntity<Integer> createCabinetType(@RequestBody CabinetType cabinetType) {
        int result = cabinetTypeService.create(cabinetType);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PutMapping("/type")
    public ResponseEntity<Integer> updateCabinetType(@RequestBody CabinetType cabinetType) {
        int result = cabinetTypeService.update(cabinetType);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/type/{code}")
    public ResponseEntity<Integer> deleteCabinetType(@PathVariable("code") String code) {
        int result = cabinetTypeService.delete(code);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // CabinetRoom 相关接口

    @GetMapping("/room")
    public ResponseEntity<List<CabinetRoom>> getAllCabinetRooms() {
        List<CabinetRoom> cabinetRooms = cabinetRoomService.findAll();
        return new ResponseEntity<>(cabinetRooms, HttpStatus.OK);
    }

    @GetMapping("/room/{id}")
    public ResponseEntity<CabinetRoom> getCabinetRoomById(@PathVariable("id") Integer id) {
        CabinetRoom cabinetRoom = cabinetRoomService.findById(id);
        if (cabinetRoom != null) {
            return new ResponseEntity<>(cabinetRoom, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    private CabinetType findCabinetTypeByCode(List<CabinetType> cabinetTypes, String code) {
        for (CabinetType cabinetType : cabinetTypes) {
            if (cabinetType.getCode().equals(code)) {
                return cabinetType;
            }
        }
        return null;
    }

    @GetMapping("/roomCompleteConfig/{id}")
    public ResponseEntity<CabinetRoom> getCabinetRoomCompleteConfigById(@PathVariable("id") Integer id) {
        List<CabinetType> cabinetTypes = cabinetTypeService.findAll();
        CabinetRoom cabinetRoom = cabinetRoomService.findById(id);
        if (cabinetRoom != null && cabinetRoom.getRows() != null) {
            try {
                // 使用Jackson解析rows字段中的JSON字符串
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rowsNode = objectMapper.readTree(cabinetRoom.getRows());

                // 遍历每个排
                if (rowsNode.isArray()) {
                    ArrayNode updatedRowsNode = objectMapper.createArrayNode();
                    for (JsonNode rowNode : rowsNode) {
                        if (rowNode.isObject() && rowNode.has("name") && rowNode.has("cabinets")) {
                            ObjectNode updatedRowNode = objectMapper.createObjectNode();
                            updatedRowNode.put("name", rowNode.get("name").asText());

                            // 处理cabinets数组
                            if (rowNode.get("cabinets").isArray()) {
                                ArrayNode updatedCabinetsNode = objectMapper.createArrayNode();
                                for (JsonNode cabinetCodeNode : rowNode.get("cabinets")) {
                                    ObjectNode cabinet = (ObjectNode)cabinetCodeNode;
                                    String cabinetTypeCode = cabinet.get("type").asText();
                                    // 根据编码获取完整的柜型配置
                                    CabinetType cabinetType = findCabinetTypeByCode(cabinetTypes, cabinetTypeCode);
                                    if (cabinetType != null) {
                                        // 将柜型配置转换为JSON对象
                                        ObjectNode cabinetNode = objectMapper.createObjectNode();
                                        cabinetNode.put("code", cabinet.get("code").asText());
                                        cabinetNode.put("name", cabinet.get("name").asText());
                                        ObjectNode cabinetTypeNode = objectMapper.createObjectNode();
                                        cabinetTypeNode.put("code", cabinetType.getCode());
                                        cabinetTypeNode.put("name", cabinetType.getName());
                                        cabinetTypeNode.put("length", cabinetType.getLength());
                                        cabinetTypeNode.put("width", cabinetType.getWidth());
                                        cabinetTypeNode.put("height", cabinetType.getHeight());
                                        cabinetTypeNode.put("color", cabinetType.getColor());
                                        cabinetTypeNode.put("image", cabinetType.getImage());
                                        cabinetNode.put("type", cabinetTypeNode);
                                        updatedCabinetsNode.add(cabinetNode);
                                    }
                                }
                                updatedRowNode.set("cabinets", updatedCabinetsNode);
                            }
                            updatedRowsNode.add(updatedRowNode);
                        }
                    }
                    // 将更新后的JSON数组转换为字符串并设置回rows字段
                    String updatedRowsJson = objectMapper.writeValueAsString(updatedRowsNode);
                    cabinetRoom.setRows(updatedRowsJson);
                }
            } catch (Exception e) {
                e.printStackTrace();
                // 如果解析失败，返回原始数据
            }
            return new ResponseEntity<>(cabinetRoom, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @PostMapping("/room")
    public ResponseEntity<Integer> createCabinetRoom(@RequestBody CabinetRoom cabinetRoom) {
        int result = cabinetRoomService.create(cabinetRoom);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PutMapping("/room")
    public ResponseEntity<Integer> updateCabinetRoom(@RequestBody CabinetRoom cabinetRoom) {
        int result = cabinetRoomService.update(cabinetRoom);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/room/{id}")
    public ResponseEntity<Integer> deleteCabinetRoom(@PathVariable("id") Integer id) {
        int result = cabinetRoomService.delete(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
