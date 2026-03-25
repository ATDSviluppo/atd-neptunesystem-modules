package com.HardwareManagerModule.HardwareManagerModule.Service;

import com.HardwareManagerModule.HardwareManagerModule.Entity.MachineConfig;
import com.HardwareManagerModule.HardwareManagerModule.Properties.MachineProperties;
import com.HardwareManagerModule.HardwareManagerModule.Repository.MachineConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class MachineConfigServiceImpl implements MachineConfigService{
    @Autowired
    private MachineConfigRepository machineConfigRepository;

    @Autowired
    private MachineProperties machineProperties;

    private final RestTemplate restTemplate;

    @Autowired
    public MachineConfigServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public List<MachineConfig> getMachineConfig() {
        return machineConfigRepository.findAll();
    }

    @Override
    public String getReaderConfig() {
        String url = "http://" + machineProperties.getIpWebService() + "/ReaderConfig";
        return restTemplate.getForObject(url, String.class);
    }
}
