package com.MailModule.MailModule.Service;

import com.MailModule.MailModule.Entity.EmployeeMailConstructor;
import com.MailModule.MailModule.Repository.MailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MailServiceImpl implements MailService {
    @Autowired
    private MailRepository mailRepository;

    @Override
    public List<EmployeeMailConstructor> findByEmailNotNullOrEmpty() {
        return mailRepository.findByEmailNotNullOrEmpty();
    }

    @Override
    public boolean setupEmail(String employeeId, String email) {
        int count = mailRepository.setupEmail(employeeId, email);
        return count > 0;
    }
}
