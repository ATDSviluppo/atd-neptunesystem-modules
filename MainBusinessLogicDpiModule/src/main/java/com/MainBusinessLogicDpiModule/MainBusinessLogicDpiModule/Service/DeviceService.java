package com.MainBusinessLogicDpiModule.MainBusinessLogicDpiModule.Service;

import com.CommonModule.CommonModule.Entity.Device;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public interface DeviceService {
    List<Device> getDevices();

    ResponseEntity<String> addDevice(Object payload);

    ResponseEntity<String> updateDevice(List<Map<String, Object>> payload);

    void deleteDevice(List<Map<String, Object>> payload);

    @Transactional
    void deleteAllDevice();
}
