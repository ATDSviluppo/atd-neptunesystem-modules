package com.CommonModule.CommonModule.DTO;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.Data;

@Data
public class EmployeeDTO {

    private String employeeId;

    private String employeeRole;

    private String employeeName;

    private String employeeCard;

}
