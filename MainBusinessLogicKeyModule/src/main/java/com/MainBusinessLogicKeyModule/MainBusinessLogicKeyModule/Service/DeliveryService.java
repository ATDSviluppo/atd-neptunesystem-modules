package com.MainBusinessLogicKeyModule.MainBusinessLogicKeyModule.Service;


import com.CommonModule.CommonModule.DTO.*;
import com.CommonModule.CommonModule.Entity.Device;
import com.CommonModule.CommonModule.Entity.Employee;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public interface DeliveryService {
    void setDeviceToTurnBack(Device device);

    @Transactional
    boolean analizeUserTurnBack(String deviceGuid) throws JsonProcessingException, InterruptedException, IOException;

    //ResponseEntity<String> handleDeviceTurnBack(Device device, String employeeCard, ObjectMapper objectMapper) throws InterruptedException, JsonProcessingException;

    boolean updateDeviceOn(String deviceId);

    ResponseEntity<String> updatePlanningStatus(List<Map<String, Object>> payload);

    boolean updateDeviceOut(String deviceId, String employeeId);

}
