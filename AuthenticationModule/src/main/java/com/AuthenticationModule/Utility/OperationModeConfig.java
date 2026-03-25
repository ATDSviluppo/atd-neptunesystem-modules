package com.AuthenticationModule.Utility;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OperationModeConfig {
    @Bean
    public OperationMode operationMode() {
        return OperationMode.RETREAT; // Imposta un valore di default
    }
}
