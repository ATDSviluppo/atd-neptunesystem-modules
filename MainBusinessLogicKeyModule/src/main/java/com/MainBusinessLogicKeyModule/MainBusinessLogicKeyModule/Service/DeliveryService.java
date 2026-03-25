package com.MainBusinessLogicKeyModule.MainBusinessLogicKeyModule.Service;


import com.CommonModule.CommonModule.DTO.*;
import com.CommonModule.CommonModule.Entity.Device;
import com.CommonModule.CommonModule.Entity.Employee;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface DeliveryService {
    void setDeviceToTurnBack(Device device);

    Device getDeviceToTurnBack();

    UserChoiceDTO analizeUserChoice(Employee employee) throws JsonProcessingException;

    @Transactional
    ResponseEntity<String> analizeUserRetreat(Map<String, Object> payload) throws
            Exception;

    @Transactional
    boolean analizeUserTurnBack(String deviceGuid) throws JsonProcessingException, InterruptedException;

    boolean analizeUserCharge(String deviceGuid) throws JsonProcessingException, InterruptedException;


    //ResponseEntity<String> handleDeviceTurnBack(Device device, String employeeCard, ObjectMapper objectMapper) throws InterruptedException, JsonProcessingException;

    List<Employee> getEmployeeForAssistantRetreat();

    boolean updateDeviceOn(String deviceId);

    boolean updateDeviceOut(String deviceId, String employeeId);

}
