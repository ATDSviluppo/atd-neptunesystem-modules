package com.MailModule.MailModule.Controller;

import com.MailModule.MailModule.Entity.EmployeeMailConstructor;
import com.MailModule.MailModule.Service.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;


@RestController
@Slf4j
public class MailController {
    @Autowired
    private MailService mailService;

    @CrossOrigin(origins = "*")
    @GetMapping("/getEmployeeWithEmailNotNull")
    public List<EmployeeMailConstructor> getEmployeeWithEmailNotNull() {
       return mailService.findByEmailNotNullOrEmpty();
    }
}
