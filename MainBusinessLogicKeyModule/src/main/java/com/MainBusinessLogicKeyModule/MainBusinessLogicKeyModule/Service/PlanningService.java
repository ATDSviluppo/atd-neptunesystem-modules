package com.MainBusinessLogicKeyModule.MainBusinessLogicKeyModule.Service;

import com.MainBusinessLogicKeyModule.MainBusinessLogicKeyModule.Entity.Planning;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public interface PlanningService {
    List<Planning> getPlanning();

    @Transactional
    void addPlanning(List<Map<String, Object>> payload);

    @Transactional
    void updatePlanning(List<Map<String, Object>> payload);

    @Transactional
    void deletePlanning(List<Map<String, Object>> payload);

    Planning getFirstActivePlanByEmployeeId(String employeeId);
}
