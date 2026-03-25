package com.AuthenticationModule.Properties;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "app")
//@PropertySource("classpath:auth.properties")
@PropertySource("file:./config/custom.properties")
public class AuthenticationProperties {
    private int cutFromFirst;

    private int cutFromLast;

    private String paddingFromFirst;

    private String paddingFromLast;

    @PostConstruct
    public void init() {
        System.out.println("AuthenticationProperties Loaded: " + this);
    }
}
