package com.CommonModule.CommonModule.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "Device")
@Data
public class Device {
    @Id
    @Column(name = "DeviceId")
    private String DeviceId;

    @Column(name = "DrumId")
    private Integer DrumId;

    @Column(name = "SectorId")
    private Integer SectorId;

    @Column(name = "ExpirationDate")
    private String ExpirationDate;

    @Column(name = "NominalNumber")
    private Integer NominalNumber;

    @Column(name = "Holder")
    private Boolean Holder;

    @Column(name = "TemporaryOwner")
    private String TemporaryOwner;

    @Column(name = "EcpCode")
    private String EcpCode;

    @Column(name = "Status")
    private String Status;

    @Column(name = "DeviceBarCode")
    private String DeviceBarCode;

    @Column(name = "MachineId")
    private String MachineId;

    @Column(name = "Location")
    private String Location;

    @Column(name = "DeviceTypeId")
    private String DeviceType;

    @Column(name = "DeviceDetailId")
    private String DeviceDetail;
}
