package com.CommonModule.CommonModule.DTO;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.Data;

@Data
public class DeviceTypeDTO {

    private String DeviceTypeId;

    private byte[] DeviceTypeImage;

    private String deviceVideoName;

    private String Description;

    private String turnBack;

    private Integer autoResetDay;

}
