package com.device.manage.controller;

import com.device.manage.entity.CabinetRoom;
import com.device.manage.entity.CabinetType;
import com.device.manage.service.CabinetRoomService;
import com.device.manage.service.CabinetTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cabinet")
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