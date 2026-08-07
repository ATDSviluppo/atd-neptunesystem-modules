package com.MainBusinessLogicKeyModule.MainBusinessLogicKeyModule.Controller;

import com.CommonModule.CommonModule.DTO.EmployeeDTO;
import com.CommonModule.CommonModule.Service.CommonService;
import com.MainBusinessLogicKeyModule.MainBusinessLogicKeyModule.Service.DeliveryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    private CommonService commonService;

    @Autowired
    private DeliveryService deliveryService;

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
    @PostMapping("UpdatePlanningStatus")
    public ResponseEntity<String> updatePlanningStatus(@RequestBody List<Map<String, Object>> payload) throws Exception {
        System.out.println("Received payload: " + payload);

        return deliveryService.updatePlanningStatus(payload);
    }


}
