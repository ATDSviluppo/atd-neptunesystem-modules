package com.MainBusinessLogicKeyModule.MainBusinessLogicKeyModule.Controller;

import com.MainBusinessLogicKeyModule.MainBusinessLogicKeyModule.Entity.Planning;
import com.MainBusinessLogicKeyModule.MainBusinessLogicKeyModule.Service.PlanningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class PlanningController {
    @Autowired
    private PlanningService planningService;

    @CrossOrigin(origins = "*")
    @GetMapping("/Planning")
    public List<Planning> getPlanning() {
        return planningService.getPlanning();
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/Planning")
    public ResponseEntity<String> addPlanning(@RequestBody List<Map<String, Object>> payload) {
        return planningService.addPlanning(payload);
    }

    @CrossOrigin(origins = "*")
    @PutMapping("/Planning")
    public ResponseEntity<String> updatePlanning(@RequestBody List<Map<String, Object>> payload) {
        return planningService.updatePlanning(payload);
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping("/Planning")
    public ResponseEntity<String> deletePlanning(@RequestBody List<Map<String, Object>> payload) {
        return planningService.deletePlanning(payload);
    }
}
