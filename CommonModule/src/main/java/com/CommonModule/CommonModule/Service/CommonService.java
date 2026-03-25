package com.CommonModule.CommonModule.Service;

import com.CommonModule.CommonModule.DTO.EmployeeDTO;
import com.CommonModule.CommonModule.DTO.UserChoiceDTO;
import com.CommonModule.CommonModule.Entity.Employee;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public interface CommonService {

    UserChoiceDTO analizeUserChoice(Employee employeeId) throws JsonProcessingException;

    @Transactional
    ResponseEntity<String> analizeUserRetreat(Map<String, Object> payload) throws
            Exception;

    @Transactional
    boolean analizeUserTurnBack(String deviceGuid) throws IOException, InterruptedException;

    boolean analizeUserCharge(String deviceGuid) throws JsonProcessingException, InterruptedException;

    List<EmployeeDTO> getEmployeeForAssistantRetreat();

    boolean updateDeviceOn(String deviceId);

    boolean updateDeviceOut(String deviceId, String employeeId);

}
