package com.MailModule.Service;

import com.MailModule.Entity.EmployeeMailConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MailService {
    List<EmployeeMailConstructor> findByEmailNotNullOrEmpty();

    boolean setupEmail(String employeeId, String email);
}
