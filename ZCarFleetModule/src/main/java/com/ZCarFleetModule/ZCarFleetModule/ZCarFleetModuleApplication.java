package com.ZCarFleetModule.ZCarFleetModule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ZCarFleetModuleApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZCarFleetModuleApplication.class, args);
	}

}
