package com.HardwareManagerModule.HardwareManagerModule.Service;

import com.HardwareManagerModule.HardwareManagerModule.Entity.MachineConfig;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MachineConfigService {
    List<MachineConfig> getMachineConfig();

    String getReaderConfig();
}
