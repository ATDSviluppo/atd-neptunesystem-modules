package com.HMIModule;

import com.CommonModule.CommonModule.Properties.BusinessProperties;
import com.HMIModule.Properties.HMIProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class ConnectionController {
    @Autowired
    BusinessProperties businessProperties;

    @CrossOrigin(origins = "*")
    @GetMapping("/api/keepalive")
    public ResponseEntity<Map<String, Object>> keepAlive() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "ok");
        response.put("machineId", businessProperties.getMachineId());
        response.put("truckingOn", businessProperties.isTruckingOn());
        return ResponseEntity.ok(response);
    }

}
