package com.MainBusinessLogicDpiModule.MainBusinessLogicDpiModule.Controller;

import com.CommonModule.CommonModule.DTO.EmployeeDTO;
import com.CommonModule.CommonModule.Service.CommonService;
import com.MainBusinessLogicDpiModule.MainBusinessLogicDpiModule.Service.DeliveryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class DeliveryController {
    @Autowired
    private DeliveryService deliveryService;

    @Autowired
    private CommonService commonService;

    @CrossOrigin(origins = "*")
    @PostMapping("/api/receive/devices")
    public void receiveDevices(@RequestBody Map<String, Object> payload) throws Exception {
        System.out.println("Received payload: " + payload);
        commonService.analizeUserRetreat(payload);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/api/send/employee")
    public List<EmployeeDTO> getEmployeeForAssistantRetreat() {
        return commonService.getEmployeeForAssistantRetreat();
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/api/massive/charge")
    public boolean massiveCharge() throws JsonProcessingException {
        return deliveryService.analizeMassiveCharger();
    }

}
