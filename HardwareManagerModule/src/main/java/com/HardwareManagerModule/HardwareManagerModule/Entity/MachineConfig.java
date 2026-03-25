package com.HardwareManagerModule.HardwareManagerModule.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "MachineConfig")
@Data
public class MachineConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long Id;

    @Column(name = "MachineId")
    private String machineId;

    @Column(name = "ParameterName")
    private String parameterName;

    @Column(name = "ParameterType")
    private String parameterType;

    @Column(name = "ParameterValue")
    private String parameterValue;

}
