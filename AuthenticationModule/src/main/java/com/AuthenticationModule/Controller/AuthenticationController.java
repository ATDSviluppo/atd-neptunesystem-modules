package com.AuthenticationModule.Controller;

import com.AuthenticationModule.Repository.EmployeeRepository;
import com.AuthenticationModule.Service.AuthenticationService;
import com.CommonModule.CommonModule.Entity.Employee;
import com.CommonModule.CommonModule.Service.CommonService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@Slf4j
public class AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private CommonService deliveryService;

    @CrossOrigin(origins = "*")
    @PostMapping("/api/badge")
    public ResponseEntity<String> receiveBadge(@RequestBody Map<String, String> badgePayload) throws Exception {
        String sourceId = badgePayload.get("SourceId");
        String employeeCard = badgePayload.get("Code");
        log.info("employeeCard" + employeeCard);
        if (employeeCard != null && !employeeCard.isEmpty()) {
            authenticationService.analizeUserAuthentication(employeeCard);
        } else {
            log.error("Valore del badge non letto");
            return ResponseEntity.status(404).body("Valore del badge non letto");
        }
        return null;
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/api/devices")
    public void receiveDeviceCode(@RequestBody Map<String, String> badgePayload) throws IOException, InterruptedException {
        Employee employee = null;
        if (authenticationService.getBadgeCode() != null) {
            employee = employeeRepository.findByEmployeeCard(authenticationService.getBadgeCode());
        }

        String sourceId = badgePayload.get("SourceId");
        String deviceCode = badgePayload.get("Code");
        if (!authenticationService.isUserAuthenticate() || authenticationService.isUserAuthenticate() && Objects.requireNonNull(employee).getEmployeeRole().equals("User") || Objects.requireNonNull(employee).getEmployeeRole().equals("Assistant")) {
            log.info(badgePayload.toString());
            deliveryService.analizeUserTurnBack(deviceCode);
        } else {
            if (Objects.equals(employee.getEmployeeRole(), "Charger")) {
                deliveryService.analizeUserCharge(deviceCode);
            }
        }
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/api/logout")
    public ResponseEntity<Map<String, String>> logout() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "ok");
        return ResponseEntity.ok(response);
    }
}
