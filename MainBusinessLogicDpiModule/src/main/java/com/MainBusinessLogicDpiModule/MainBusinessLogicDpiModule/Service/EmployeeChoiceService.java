package com.MainBusinessLogicDpiModule.MainBusinessLogicDpiModule.Service;

import com.MainBusinessLogicDpiModule.MainBusinessLogicDpiModule.Entity.EmployeeChoice;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface EmployeeChoiceService {
    List<EmployeeChoice> getEmployeeChoice();
    @Transactional
    void addEmployeeChoice(List<Map<String, Object>> payloadList);

    @Transactional
    void updateEmployeeChoice(List<Map<String, Object>> payloadList);

    @Transactional
    void deleteEmployeeChoice(List<Map<String, Object>> payloadList);

    @Transactional
    void deleteAllEmployeeChoice();
}
