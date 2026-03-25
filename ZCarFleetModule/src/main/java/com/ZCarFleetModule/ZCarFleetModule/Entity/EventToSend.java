package com.ZCarFleetModule.ZCarFleetModule.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "EventToSend")
@Data
public class EventToSend {
    @Id
    @Column(name = "eventId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventId;

    @Column(name = "eventTimestamp")
    private String eventTimestamp;

    @Column(name = "eventType")
    private String eventType;

    @Column(name = "deviceId")
    private String deviceId;

    @Column(name = "employeeId")
    private String employeeId;

    @Column(name = "planningId")
    private String planningId;

    @Column(name = "deviceDetailId")
    private String deviceDetailId;

    @Column(name = "enumDeviceTypeId")
    private String enumDeviceTypeId;

    @Column(name = "deviceBarCode")
    private String deviceBarCode;

    @Column(name = "location", nullable = true)
    private String location;

    @Column(name = "machineId", nullable = true)
    private String machineId;

    @Column(name = "note")
    private String note;
}
