package com.CommonModule.CommonModule.Properties;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "business")
//@PropertySource("classpath:business.properties")
@PropertySource("file:./config/custom.properties")
public class BusinessProperties {

    private boolean truckingOn;

    private String machineId;


    @PostConstruct
    public void init() {
        System.out.println("BusinessProperties Loaded: " + this);
    }
}
