package com.CommonModule.CommonModule.DTO;

import lombok.Data;

import java.util.List;

@Data
public class UserChoiceDTO {
    private List<DeviceDTO> deviceList;

    private List<DeviceTypeDTO> enumDeviceTypeList;

    private EmployeeDTO employee;
}
