package com.MainBusinessLogicDpiModule.MainBusinessLogicDpiModule.Service;

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
    ResponseEntity<String> addEmployee(Object payload);

    @Transactional
    ResponseEntity<String> updateEmployee(Object payloadList);

    @Transactional
    ResponseEntity<String> deleteEmployee(Object payloadList);
}
