package com.SMTPServerManager.Properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Data
@Configuration
@ConfigurationProperties(prefix = "smtp")
@PropertySource("file:./config/smtp.properties")
//@PropertySource("classpath:smtp.properties")

public class MailProperties {

    @Value("${smtp.mailServerHost}")
    private String mailServerHost;

    @Value("${smtp.mailServerPort}")
    private String mailServerPort;

    @Value("${smtp.mailSender}")
    private String mailSender;

    @Value("${smtp.mailPassword}")
    private String mailPassword;
}
