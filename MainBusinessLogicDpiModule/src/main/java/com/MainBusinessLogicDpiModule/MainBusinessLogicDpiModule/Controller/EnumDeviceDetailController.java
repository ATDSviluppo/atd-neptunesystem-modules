package com.MainBusinessLogicDpiModule.MainBusinessLogicDpiModule.Controller;


import com.CommonModule.CommonModule.Entity.EnumDeviceDetail;
import com.MainBusinessLogicDpiModule.MainBusinessLogicDpiModule.Service.EnumDeviceDetailService;
import com.MainBusinessLogicDpiModule.MainBusinessLogicDpiModule.Service.EnumDeviceTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class EnumDeviceDetailController {

    @Autowired
    EnumDeviceDetailService enumDeviceDetailService;

    @GetMapping("/EnumDeviceDetail")
    public List<EnumDeviceDetail> getEnumDeviceDetail()
    {
        return enumDeviceDetailService.getEnumDeviceDetail();
    }

    @PostMapping("/EnumDeviceDetail")
    public void insertEnumDeviceDetail(@RequestBody List<Map<String, Object>> payload)
    {
        enumDeviceDetailService.addEnumDeviceDetail(payload);
    }

    @PutMapping("/EnumDeviceDetail")
    public void modifyEnumDeviceDetail(@RequestBody List<Map<String, Object>> payload)
    {
        enumDeviceDetailService.updateEnumDeviceDetail(payload);
    }

    @DeleteMapping("/EnumDeviceDetail")
    public void deleteEnumDeviceType(@RequestBody List<Map<String, Object>> payload)
    {
        enumDeviceDetailService.deleteEnumDeviceDetail(payload);
    }
}
