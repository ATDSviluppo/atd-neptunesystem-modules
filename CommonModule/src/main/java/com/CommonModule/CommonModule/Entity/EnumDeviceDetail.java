package com.CommonModule.CommonModule.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "EnumDeviceDetail")
@Data
public class EnumDeviceDetail {

    @Id
    @Column(name = "DeviceDetailId")
    private String DeviceDetailId;

    @Column(name = "Description")
    private String Description;
}
