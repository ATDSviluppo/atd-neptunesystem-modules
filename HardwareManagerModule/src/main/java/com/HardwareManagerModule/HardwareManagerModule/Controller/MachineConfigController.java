package com.HardwareManagerModule.HardwareManagerModule.Controller;

import com.HardwareManagerModule.HardwareManagerModule.Entity.MachineConfig;
import com.HardwareManagerModule.HardwareManagerModule.Service.MachineConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MachineConfigController {
    @Autowired
    private MachineConfigService machineConfigService;

    @CrossOrigin(origins = "*")
    @GetMapping("/MachineConfig")
    public List<MachineConfig> getMachineConfig() {
        return machineConfigService.getMachineConfig();
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/ReaderConfig")
    public String getReaderConfig() {
        return machineConfigService.getReaderConfig();
    }
}
