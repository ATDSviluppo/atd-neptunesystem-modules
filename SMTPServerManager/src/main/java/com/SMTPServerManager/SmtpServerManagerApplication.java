package com.SMTPServerManager;

import com.SMTPServerManager.Properties.MailProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableConfigurationProperties(MailProperties.class)
@SpringBootApplication(scanBasePackages = "com.SMTPServerManager")
@EnableScheduling
public class SmtpServerManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmtpServerManagerApplication.class, args);
	}

}
