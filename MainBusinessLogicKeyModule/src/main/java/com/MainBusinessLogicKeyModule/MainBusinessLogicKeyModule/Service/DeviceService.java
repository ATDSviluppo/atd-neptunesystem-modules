package com.MainBusinessLogicKeyModule.MainBusinessLogicKeyModule.Service;


import com.CommonModule.CommonModule.Entity.Device;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface DeviceService {
    List<Device> getDevices();

    ResponseEntity<String> addDevice(Object payload);

    ResponseEntity<String> updateDevice(Object payload);

    ResponseEntity<String> deleteDevice(Map<String, Object> payload);

}
