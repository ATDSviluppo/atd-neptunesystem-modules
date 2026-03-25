package com.MainBusinessLogicDpiModule.MainBusinessLogicDpiModule.Mapper;

import com.CommonModule.CommonModule.DTO.*;
import com.CommonModule.CommonModule.Entity.Device;
import com.CommonModule.CommonModule.Entity.Employee;
import com.CommonModule.CommonModule.Entity.EnumDeviceDetail;
import com.CommonModule.CommonModule.Entity.EnumDeviceType;


import java.util.List;

public class DeviceMapper {
    public static UserChoiceDTO getChoiceDTO(List<DeviceTypeDTO> enumDeviceTypeList, List<DeviceDTO> deviceList, EmployeeDTO employee) {
        UserChoiceDTO userChoiceDTO = new UserChoiceDTO();
        userChoiceDTO.setDeviceList(deviceList);
        userChoiceDTO.setEnumDeviceTypeList(enumDeviceTypeList);
        userChoiceDTO.setEmployee(employee);

        return userChoiceDTO;
    }

    public static UserChoiceDTO getUserChoiceDTO(List<DeviceTypeDTO> enumDeviceTypeList, List<DeviceDTO> deviceList, EmployeeDTO employee) {
        return getChoiceDTO(enumDeviceTypeList, deviceList, employee);
    }

    public static DeviceDTO setDeviceDTO(Device device, List<DeviceDetailDTO> enumDeviceDetailList) {
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
        deviceDTO.setEnumDeviceDetailList(enumDeviceDetailList);

        return deviceDTO;
    }

    public static DeviceDetailDTO setDetailDTO(EnumDeviceDetail enumDeviceDetail, int qta) {
        DeviceDetailDTO deviceDetailDTO = new DeviceDetailDTO();
        deviceDetailDTO.setDeviceDetailId(enumDeviceDetail.getDeviceDetailId());
        deviceDetailDTO.setDescription(enumDeviceDetail.getDescription());
        deviceDetailDTO.setQta(qta);

        return deviceDetailDTO;
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
