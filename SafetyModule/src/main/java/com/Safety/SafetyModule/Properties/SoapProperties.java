package com.Safety.SafetyModule.Properties;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "soap")
//@PropertySource("classpath:soap.properties")
@PropertySource("file:./config/custom.properties")
public class SoapProperties {
    private String UserName;

    private String Password;

    private String Company;

    private String UrlWeb;

    private Long timeToScanEventTable;

    @PostConstruct
    public void init() {
        System.out.println("SoapProperties Loaded: " + this);
    }
}
