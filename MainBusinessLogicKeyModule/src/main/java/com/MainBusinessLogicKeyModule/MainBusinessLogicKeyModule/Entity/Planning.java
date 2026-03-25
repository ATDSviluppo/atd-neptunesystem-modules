package com.MainBusinessLogicKeyModule.MainBusinessLogicKeyModule.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Planning")
public class Planning {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long Id;

    @Column(name = "planningId", nullable = false)
    private String PlanningId;

    @Column(name = "planningDate", nullable = false)
    private String PlanningDate;

    @Column(name = "startPlan", nullable = false)
    private String StartPlan;

    @Column(name = "stopPlan", nullable = false)
    private String StopPlan;

    @Column(name = "deviceId", nullable = false)
    private String DeviceId;

    @Column(name = "employeeId")
    private String EmployeeId;

    @Column(name = "enumDeviceTypeId")
    private String EnumDeviceType;
}
