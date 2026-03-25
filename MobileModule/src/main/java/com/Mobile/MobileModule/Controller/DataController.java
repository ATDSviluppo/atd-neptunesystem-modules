package com.Mobile.MobileModule.Controller;

import com.AuthenticationModule.Repository.EmployeeRepository;
import com.CommonModule.CommonModule.Entity.Employee;
import com.Mobile.MobileModule.DTO.DeviceDTO;
import com.Mobile.MobileModule.DTO.EventDTO;
import com.Mobile.MobileModule.Service.MobileService;
import com.Mobile.MobileModule.Service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DataController {
    @Autowired
    private MobileService deviceService;

    @CrossOrigin(origins = "*")
    @GetMapping("/api/getDevices")
    public List<DeviceDTO> getDevices() {
        return deviceService.getDevice();
    }

    @Autowired
    private EventService eventService;

    @CrossOrigin(origins = "*")
    @GetMapping("/api/getEvents")
    public ResponseEntity<List<EventDTO>> getEvents() {
        return eventService.getEvents();
    }

    @Autowired
    private EmployeeRepository employeeRepository;

    @CrossOrigin(origins = "*")
    @GetMapping("/api/getEmployee")
    public List<Employee> getEmployee()
    {
        return employeeRepository.findAll();
    }
}
