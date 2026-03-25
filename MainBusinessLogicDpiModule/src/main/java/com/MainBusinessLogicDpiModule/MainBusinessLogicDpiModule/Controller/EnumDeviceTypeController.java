package com.MainBusinessLogicDpiModule.MainBusinessLogicDpiModule.Controller;


import com.CommonModule.CommonModule.Entity.EnumDeviceType;
import com.MainBusinessLogicDpiModule.MainBusinessLogicDpiModule.Service.EnumDeviceTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class EnumDeviceTypeController {

    @Autowired
    EnumDeviceTypeService enumDeviceTypeService;

    @GetMapping("/EnumDeviceType")
    public List<EnumDeviceType> getEnumDeviceType() {
        return enumDeviceTypeService.getEnumDeviceType();
    }

    @PostMapping("/EnumDeviceType")
    public void insertEnumDeviceType(@RequestBody List<Map<String, Object>> payload) {
        enumDeviceTypeService.addEnumDeviceType(payload);
    }

    @PutMapping("/EnumDeviceType")
    public void modifyEnumDeviceType(@RequestBody List<Map<String, Object>> payload) {
        enumDeviceTypeService.updateEnumDeviceType(payload);
    }

    @DeleteMapping("/EnumDeviceType")
    public void deleteEnumDeviceType(@RequestBody List<Map<String, Object>> payload) {
        enumDeviceTypeService.deleteEnumDeviceTypes(payload);
    }
}
