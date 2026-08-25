package com.NeptuneWebAutheticator.Controller;

import com.NeptuneWebAutheticator.DTO.MachineDTO;
import com.NeptuneWebAutheticator.Service.MachineService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
public class MachineController {
    @Autowired
    private MachineService machineService;

    @PostMapping("/Machine")
    //@CrossOrigin(origins = "http://localhost:5555", allowCredentials = "true")
    public ResponseEntity<String> addMachine(HttpServletRequest request, @RequestBody Map<String, Object> payload) {
        return machineService.addMachine(request, payload);
    }

    @GetMapping("/Machine")
    //@CrossOrigin(origins = "http://localhost:5555", allowCredentials = "true")
    public List<MachineDTO> getMachines(HttpServletRequest request) {
        return machineService.getMachinesByTenantId(request);
    }

    @DeleteMapping("/Machine")
    //@CrossOrigin(origins = "http://localhost:5555", allowCredentials = "true")
    public ResponseEntity<String> deleteMachine(HttpServletRequest request, @RequestBody Map<String, Object> payload) {
        return machineService.deleteMachine(request, payload);
    }

    @PostMapping("/api/Tenant")
    public ResponseEntity<String> addTenant(HttpServletRequest userId, @RequestBody Map<String, Object> payload) {
        return machineService.addTenant(userId, payload);
    }

    @PutMapping("/api/Tenant")
    public ResponseEntity<String> updateTenant(HttpServletRequest userId, @RequestBody Map<String, Object> payload) {
        return machineService.updateTenant(userId, payload);
    }

    @GetMapping("/api/modules")
    public ResponseEntity<List<String>> getModules() throws IOException {
        return machineService.getPlugin();
    }

    @GetMapping("/api/modules/{jarName}")
    public ResponseEntity<Resource> getJarPlugin(
            @PathVariable String jarName
    ) {
        return machineService.getJarPlugin(jarName);
    }
}
