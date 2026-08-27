package com.MailModule.Service;

import com.MailModule.Entity.EmployeeMailConstructor;
import com.MailModule.Properties.MailProperties;
import com.MailModule.Repository.MailRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class MailServiceImpl implements MailService {
    @Autowired
    private MailRepository mailRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private MailProperties mailProperties;

    @Override
    public List<EmployeeMailConstructor> findByEmailNotNullOrEmpty() {
        return mailRepository.findByEmailNotNullOrEmpty();
    }

    @Override
    public boolean setupEmail(String employeeId, String email) {
        int count = mailRepository.setupEmail(employeeId, email);
        return count > 0;
    }

    @Scheduled(
            cron = "0 0 9 * * *",
            zone = "Europe/Rome"
    )
    @Override
    public List<String> getEmailToSendExpirationAdvice(int daysBeforeExpiration) throws JsonProcessingException {
        Date currentDate = Date.valueOf(LocalDate.now());
        String url = "https://neptunesystem.zcsautomation.com/api/sendMail";
        List<String> toAddress = mailRepository.getEmailToSendExpirationAdvice(
                daysBeforeExpiration,
                currentDate
        );

        String body = mailProperties.getBody();
        String subject = mailProperties.getSubject();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> request = new HashMap<>();
        request.put("Body", body);
        request.put("Subject", subject);
        request.put("ToAddress", toAddress);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonPayload = objectMapper.writeValueAsString(request);

        HttpEntity<String> requestEntity = new HttpEntity<>(jsonPayload, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            log.info("Email inviata con successo ai seguenti destinatari " + toAddress);
        } else {
            log.info("Server non raggiungibile");
        }

        return toAddress;
    }
}
