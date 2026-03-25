package com.MainBusinessLogicDpiModule.MainBusinessLogicDpiModule.Controller;

import com.MainBusinessLogicDpiModule.MainBusinessLogicDpiModule.Entity.EmployeeChoice;
import com.MainBusinessLogicDpiModule.MainBusinessLogicDpiModule.Service.EmployeeChoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class EmployeeChoiceController {

    @Autowired
    private EmployeeChoiceService employeeChoiceService;

    @CrossOrigin(origins = "*")
    @GetMapping("/EmployeeChoice")
    public List<EmployeeChoice> getEmployeeChoice() {
        return employeeChoiceService.getEmployeeChoice();
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/EmployeeChoice")
    public void addEmployeeChoice(@RequestBody List<Map<String, Object>> payload) {
        employeeChoiceService.addEmployeeChoice(payload);
    }

    @CrossOrigin(origins = "*")
    @PutMapping("/EmployeeChoice")
    public void updateEmployeeChoice(@RequestBody List<Map<String, Object>> payload) {
        employeeChoiceService.updateEmployeeChoice(payload);
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping("/EmployeeChoice")
    public void deleteEmployeeChoice(@RequestBody(required = false) List<Map<String, Object>> payload) {
        if (payload != null) {
            employeeChoiceService.deleteEmployeeChoice(payload);
        } else {
            employeeChoiceService.deleteAllEmployeeChoice();
        }
    }

}
