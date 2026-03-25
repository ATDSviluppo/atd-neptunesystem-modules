package com.MainBusinessLogicDpiModule.MainBusinessLogicDpiModule.Controller;


import com.CommonModule.CommonModule.Entity.Employee;
import com.MainBusinessLogicDpiModule.MainBusinessLogicDpiModule.Service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @CrossOrigin(origins = "*")
    @GetMapping("/Employee")
    public List<Employee> getEmployee() {
        return employeeService.getEmployee();
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/Employee")
    public ResponseEntity<String> addEmployee(@RequestBody Object payload) {
        return employeeService.addEmployee(payload);
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping("/Employee")
    public ResponseEntity<String> deleteEmployee(@RequestBody Object payload) {
        return employeeService.deleteEmployee(payload);
    }

    @CrossOrigin(origins = "*")
    @PutMapping("/Employee")
    public ResponseEntity<String> updateEmployee(@RequestBody Object payload) {
        return employeeService.updateEmployee(payload);
    }

}
