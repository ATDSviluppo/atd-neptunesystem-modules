package com.MainBusinessLogicKeyModule.MainBusinessLogicKeyModule.Service;

import com.CommonModule.CommonModule.Entity.Employee;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public interface EmployeeService {
    List<Employee> getEmployee();

    @Transactional
    void addEmployee(Object payload);

    @Transactional
    ResponseEntity<String> updateEmployee(Map<String, Object> payloadList);

    @Transactional
    ResponseEntity<String> deleteEmployee(Map<String, Object> payloadList);
}
