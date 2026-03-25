package com.MainBusinessLogicKeyModule.MainBusinessLogicKeyModule.Controller;


import com.CommonModule.CommonModule.Entity.Device;
import com.MainBusinessLogicKeyModule.MainBusinessLogicKeyModule.Service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    @CrossOrigin(origins = "*")
    @GetMapping("/Devices")
    public List<Device> getDevice() {
        return deviceService.getDevices();
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/Devices")
    public void addDevice(@RequestBody List<Map<String, Object>> payload) {
        deviceService.addDevice(payload);
    }

    @CrossOrigin(origins = "*")
    @PutMapping("/Devices")
    public ResponseEntity<String> updateDevice(@RequestBody Map<String, Object> payload) {
        return deviceService.updateDevice(payload);
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping("/Devices")
    public void deleteDevice(@RequestBody Map<String, Object> payload) {
        deviceService.deleteDevice(payload);
    }

}
