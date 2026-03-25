package com.HardwareManagerModule.HardwareManagerModule.Properties;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "machine")
@PropertySource("file:./config/custom.properties")
public class MachineProperties {

    private String ipWebService;

    private int timeCloseDoor;

    private String os;


    @PostConstruct
    public void init() {
        System.out.println("MachineProperties Loaded: " + this);
    }
}
