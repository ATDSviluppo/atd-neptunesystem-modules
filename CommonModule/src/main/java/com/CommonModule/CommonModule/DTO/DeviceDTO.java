package com.CommonModule.CommonModule.DTO;

import lombok.Data;

import java.util.List;

@Data
public class DeviceDTO {

    private String DeviceId;

    private Integer DrumId;

    private Integer SectorId;

    private String ExpirationDate;

    private Integer NominalNumber;

    private Boolean Holder;

    private String TemporaryOwner;

    private String EcpCode;

    private String Status;

    private String DeviceBarCode;

    private String DeviceType;

    private String DeviceDetail;

    private List<DeviceDetailDTO> enumDeviceDetailList;

}
