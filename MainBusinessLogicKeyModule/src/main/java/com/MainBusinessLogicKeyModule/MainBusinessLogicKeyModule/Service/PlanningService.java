package com.MainBusinessLogicKeyModule.MainBusinessLogicKeyModule.Service;

import com.MainBusinessLogicKeyModule.MainBusinessLogicKeyModule.Entity.Planning;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public interface PlanningService {
    List<Planning> getPlanning();

    @Transactional
    ResponseEntity<String> addPlanning(List<Map<String, Object>> payload);

    @Transactional
    ResponseEntity<String> updatePlanning(List<Map<String, Object>> payload);

    @Transactional
    ResponseEntity<String> deletePlanning(List<Map<String, Object>> payload);

    Planning getFirstActivePlanByEmployeeId(String employeeId);
}
