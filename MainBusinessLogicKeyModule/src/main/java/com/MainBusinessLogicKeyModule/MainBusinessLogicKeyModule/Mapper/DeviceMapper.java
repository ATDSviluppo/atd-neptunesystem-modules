package com.MainBusinessLogicKeyModule.MainBusinessLogicKeyModule.Mapper;


import com.CommonModule.CommonModule.DTO.DeviceDTO;
import com.CommonModule.CommonModule.DTO.DeviceTypeDTO;
import com.CommonModule.CommonModule.DTO.EmployeeDTO;
import com.CommonModule.CommonModule.DTO.UserChoiceDTO;
import com.CommonModule.CommonModule.Entity.Device;
import com.CommonModule.CommonModule.Entity.Employee;
import com.CommonModule.CommonModule.Entity.EnumDeviceType;

import java.util.ArrayList;
import java.util.List;

public class DeviceMapper {
    public static UserChoiceDTO getChoiceDTO(List<DeviceTypeDTO> enumDeviceTypeList, List<DeviceDTO> deviceList, EmployeeDTO employeeDTO) {
        UserChoiceDTO userChoiceDTO = new UserChoiceDTO();
        userChoiceDTO.setDeviceList(deviceList);
        userChoiceDTO.setEnumDeviceTypeList(enumDeviceTypeList);
        userChoiceDTO.setEmployee(employeeDTO);

        return userChoiceDTO;
    }

    public static UserChoiceDTO getUserChoiceDTO(List<DeviceTypeDTO> enumDeviceTypeList, List<DeviceDTO> deviceList, EmployeeDTO employeeDTO) {
        return getChoiceDTO(enumDeviceTypeList, deviceList, employeeDTO);
    }

    public static DeviceDTO setDeviceDTO(Device device) {
        DeviceDTO deviceDTO = new DeviceDTO();
        deviceDTO.setDeviceId(device.getDeviceId());
        deviceDTO.setDeviceDetail(device.getDeviceDetail());
        deviceDTO.setDeviceType(device.getDeviceType());
        deviceDTO.setDeviceBarCode(device.getDeviceBarCode());
        deviceDTO.setDrumId(device.getDrumId());
        deviceDTO.setEcpCode(device.getEcpCode());
        deviceDTO.setSectorId(device.getSectorId());
        deviceDTO.setExpirationDate(device.getExpirationDate());
        deviceDTO.setHolder(device.getHolder());
        deviceDTO.setStatus(device.getStatus());
        deviceDTO.setTemporaryOwner(device.getTemporaryOwner());
        deviceDTO.setNominalNumber(device.getNominalNumber());
        deviceDTO.setEnumDeviceDetailList(new ArrayList<>());

        return deviceDTO;
    }

    public static DeviceTypeDTO setEnumDeviceTypeDTO(EnumDeviceType device) {
        DeviceTypeDTO deviceDTO = new DeviceTypeDTO();
        deviceDTO.setDeviceTypeId(device.getDeviceTypeId());
        deviceDTO.setDescription(device.getDescription());
        deviceDTO.setDeviceTypeImage(device.getDeviceTypeImage());
        deviceDTO.setDeviceVideoName(device.getDeviceVideoName());
        deviceDTO.setTurnBack(device.getTurnBack());
        deviceDTO.setAutoResetDay(device.getAutoResetDay());

        return deviceDTO;
    }

    public static EmployeeDTO setEmployeeDTO(Employee employee) {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setEmployeeCard(employee.getEmployeeCard());
        employeeDTO.setEmployeeId(employee.getEmployeeId());
        employeeDTO.setEmployeeName(employee.getEmployeeName());
        employeeDTO.setEmployeeRole(employee.getEmployeeRole());

        return employeeDTO;
    }


}
