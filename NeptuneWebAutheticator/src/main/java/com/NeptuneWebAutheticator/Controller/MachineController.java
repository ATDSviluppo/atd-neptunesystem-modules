package com.NeptuneWebAutheticator.Controller;

import com.NeptuneWebAutheticator.DTO.MachineDTO;
import com.NeptuneWebAutheticator.Entity.Machine;
import com.NeptuneWebAutheticator.Service.MachineService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class MachineController {
    @Autowired
    private MachineService machineService;

    @PostMapping("/Machine")
    //@CrossOrigin(origins = "http://localhost:5555", allowCredentials = "true")
    public ResponseEntity<String> addMachine(HttpServletRequest request, @RequestBody  Map<String, Object> payload) {
        return machineService.addMachine(request, payload);
    }

    @GetMapping("/Machine")
    //@CrossOrigin(origins = "http://localhost:5555", allowCredentials = "true")
    public List<MachineDTO> getMachines(HttpServletRequest request) {
        return machineService.getMachinesByTenantId(request);
    }

    @DeleteMapping("/Machine")
    //@CrossOrigin(origins = "http://localhost:5555", allowCredentials = "true")
    public ResponseEntity<String> deleteMachine(HttpServletRequest request,@RequestBody  Map<String, Object> payload) {
        return machineService.deleteMachine(request, payload);
    }
}
