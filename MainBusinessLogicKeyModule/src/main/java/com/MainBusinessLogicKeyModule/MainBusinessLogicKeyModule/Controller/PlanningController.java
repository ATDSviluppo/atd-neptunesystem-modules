package com.MainBusinessLogicKeyModule.MainBusinessLogicKeyModule.Controller;

import com.MainBusinessLogicKeyModule.MainBusinessLogicKeyModule.Entity.Planning;
import com.MainBusinessLogicKeyModule.MainBusinessLogicKeyModule.Service.PlanningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class PlanningController {
    @Autowired
    private PlanningService planningService;

    @CrossOrigin(origins = "*")
    @GetMapping ("/Planning")
    public List<Planning> getPlanning()
    {
       return planningService.getPlanning();
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/Planning")
    public void addPlanning(@RequestBody List<Map<String, Object>> payload)
    {
        planningService.addPlanning(payload);
    }

    @CrossOrigin(origins = "*")
    @PutMapping("/Planning")
    public void updatePlanning(@RequestBody List<Map<String, Object>> payload)
    {
        planningService.updatePlanning(payload);
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping("/Planning")
    public void deletePlanning(@RequestBody List<Map<String, Object>> payload)
    {
        planningService.deletePlanning(payload);
    }
}
