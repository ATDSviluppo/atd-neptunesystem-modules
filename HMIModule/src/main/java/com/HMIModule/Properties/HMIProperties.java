package com.HMIModule.Properties;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Data
@Configuration
@ConfigurationProperties(prefix = "hmi")
//@PropertySource("classpath:hmi.properties")
@PropertySource("file:./config/custom.properties")
public class HMIProperties {

    private String keyword;

    @PostConstruct
    public void init() {
        System.out.println("HMIProperties Loaded: " + this);
    }
}
