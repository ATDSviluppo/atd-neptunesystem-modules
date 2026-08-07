package com.SMTPServerManager.EmailController;

import com.SMTPServerManager.Service.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;
import java.util.Map;

@RestController
public class EmailController {
    @Autowired
    private EmailService emailService;

    @PostMapping("/api/sendMail")
    public ResponseEntity<String> sendMail(@RequestBody Map<String, Object> payload) throws MessagingException {
       return emailService.sendMail(payload);
    }

}
