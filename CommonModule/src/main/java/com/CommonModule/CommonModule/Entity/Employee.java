package com.CommonModule.CommonModule.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "Employee")
@Data
public class Employee {
    @Id
    @Column(name = "employeeId")
    private String employeeId;

    @Column(name = "employeeRole")
    private String employeeRole;

    @Column(name = "employeeName")
    private String employeeName;

    @Column(name = "employeeCard")
    private String employeeCard;

}
