package com.AuthenticationModule;

import com.AuthenticationModule.Properties.AuthenticationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(AuthenticationProperties.class)
@SpringBootApplication
public class AuthenticationModuleApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthenticationModuleApplication.class, args);
	}

}
