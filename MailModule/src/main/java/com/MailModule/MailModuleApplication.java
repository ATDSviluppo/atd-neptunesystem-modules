package com.MailModule;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.context.annotation.ComponentScan;

@AutoConfiguration
@AutoConfigurationPackage
@ComponentScan("com.MailModule")
public class MailModuleApplication {
	@PostConstruct
	public void init() {
		System.out.println("=== MAIL MODULE CARICATO ===");
	}
}
