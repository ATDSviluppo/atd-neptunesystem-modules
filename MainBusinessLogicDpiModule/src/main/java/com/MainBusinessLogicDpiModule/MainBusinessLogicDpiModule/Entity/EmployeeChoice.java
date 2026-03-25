package com.MainBusinessLogicDpiModule.MainBusinessLogicDpiModule.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "EmployeeChoice")
@Data
public class EmployeeChoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employeeChoiceId")
    private Long employeeChoiceId;

    @Column(name = "DeviceTypeId")
    private String deviceTypeId;

    @Column(name = "EmployeeId")
    private String employeeId;

    @Column(name = "CreditNumber")
    private Integer creditNumber;

    @Column(name = "CreditSpent")
    private Integer creditSpent;

    @Column(name = "ResetCredit")
    private Boolean resetCredit;

    @Column(name = "RetiredDate")
    private String retiredDate;

}
