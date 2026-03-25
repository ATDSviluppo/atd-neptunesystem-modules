package com.AuthenticationModule.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface AuthenticationService {
    String getBadgeCode();

    void setBadgeCode(String badgeCode);

    ResponseEntity<String> analizeUserAuthentication(String employeeCard) throws IOException, InterruptedException;

    boolean isUserAuthenticate();

    }
