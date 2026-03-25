package com.CommonModule.CommonModule.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "EnumDeviceType")
public class EnumDeviceType {
    @Id
    @Column(name = "enumDeviceTypeId")
    private String DeviceTypeId;

    @Lob
    @Column(name = "DeviceTypeImage", columnDefinition = "MEDIUMBLOB")
    private byte[] DeviceTypeImage;

    @Column(name = "DeviceVideoName")
    private String deviceVideoName;

    @Column(name = "Description")
    private String Description;

    @Column(name = "TurnBack")
    private String turnBack;

    @Column(name = "AutoResetDay")
    private Integer autoResetDay;


}
