package com.SMTPServerManager.Service;

import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface EmailService {
    ResponseEntity<String> sendMail(Map<String, Object> payload) throws MessagingException;
}
