package com.CommonModule.CommonModule.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table(name = "Events")
public class Event {
    @Id
    @Column(name = "eventId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventId;

    @Column(name = "eventTimestamp")
    private Date eventTimestamp;

    @Column(name = "eventType", nullable = true)
    private String eventType;

    @Column(name = "deviceId", nullable = true)
    private String deviceId;

    @Column(name = "employeeId", nullable = true)
    private String employeeId;

    @Column(name = "planningId", nullable = true)
    private String planningId;

    @Column(name = "deviceDetailId", nullable = true)
    private String deviceDetailId;

    @Column(name = "enumDeviceTypeId", nullable = true)
    private String enumDeviceTypeId;

    @Column(name = "location", nullable = true)
    private String location;

    @Column(name = "machineId", nullable = true)
    private String machineId;

    @Column(name = "note", nullable = true)
    private String note;

    @Column(name = "toSend", nullable = true)
    private boolean toSend;

    public Event() {
    }

    public Event(Date eventTimestamp, String eventType, String deviceId, String employeeId, String deviceDetailId, String enumDeviceTypeId, String location, String machineId, String note, boolean toSend) {
        this.eventTimestamp = eventTimestamp;
        this.eventType = eventType;
        this.deviceId = deviceId;
        this.employeeId = employeeId;
        this.deviceDetailId = deviceDetailId;
        this.enumDeviceTypeId = enumDeviceTypeId;
        this.location = location;
        this.machineId = machineId;
        this.note = note;
        this.toSend = toSend;
    }

    public Event(Date eventTimestamp, String eventType, String deviceId, String employeeId, String planningId, String deviceDetailId, String enumDeviceTypeId, String location, String machineId, String note, boolean toSend) {
        this.eventTimestamp = eventTimestamp;
        this.eventType = eventType;
        this.deviceId = deviceId;
        this.employeeId = employeeId;
        this.planningId = planningId;
        this.deviceDetailId = deviceDetailId;
        this.enumDeviceTypeId = enumDeviceTypeId;
        this.location = location;
        this.machineId = machineId;
        this.note = note;
        this.toSend = toSend;
    }

    public Event(Date eventTimestamp, String eventType, String deviceId,
                 String deviceDetailId, String enumDeviceTypeId, String location, String machineId, String note, boolean toSend) {
        this.eventTimestamp = eventTimestamp;
        this.eventType = eventType;
        this.deviceId = deviceId;
        this.deviceDetailId = deviceDetailId;
        this.enumDeviceTypeId = enumDeviceTypeId;
        this.location = location;
        this.machineId = machineId;
        this.note = note;
        this.toSend = toSend;
    }
}