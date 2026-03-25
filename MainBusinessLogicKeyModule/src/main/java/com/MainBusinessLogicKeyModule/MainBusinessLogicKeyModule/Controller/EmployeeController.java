package com.MainBusinessLogicKeyModule.MainBusinessLogicKeyModule.Controller;


import com.CommonModule.CommonModule.Entity.Employee;
import com.MainBusinessLogicKeyModule.MainBusinessLogicKeyModule.Service.EmployeeService;
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
    public List<Employee> getEmployee()
    {
        return employeeService.getEmployee();
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/Employee")
    public void addEmployee(@RequestBody List<Map<String, Object>> payload)
    {
        employeeService.addEmployee(payload);
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping("/Employee")
    public void deleteEmployee(@RequestBody Map<String, Object> payload)
    {
        employeeService.deleteEmployee(payload);
    }

    @CrossOrigin(origins = "*")
    @PutMapping("/Employee")
    public ResponseEntity<String> updateEmployee(@RequestBody Map<String, Object> payload)
    {
       return employeeService.updateEmployee(payload);
    }

}
