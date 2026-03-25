package com.MainBusinessLogicKeyModule.MainBusinessLogicKeyModule.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "PlanningRetriedMap")
@Data
public class PlanningRetriedMap {
    @Id
    @Column(name = "PlanningId", nullable = false, unique = true)
    private String PlanningId;

    @Column(name = "DeviceId", nullable = false)
    private String DeviceId;

    @Column(name = "EmployeeId", nullable = false)
    private String EmployeeId;

    public PlanningRetriedMap() {
    }

    public PlanningRetriedMap(String planningId, String deviceId, String employeeId) {
        PlanningId = planningId;
        DeviceId = deviceId;
        EmployeeId = employeeId;
    }
}
