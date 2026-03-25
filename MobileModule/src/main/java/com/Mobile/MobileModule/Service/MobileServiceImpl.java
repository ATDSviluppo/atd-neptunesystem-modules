package com.Mobile.MobileModule.Service;

import com.CommonModule.CommonModule.Entity.Device;
import com.CommonModule.CommonModule.Properties.BusinessProperties;
import com.CommonModule.CommonModule.Repository.CommonRepository;
import com.Mobile.MobileModule.DTO.DeviceDTO;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MobileServiceImpl implements MobileService {
    @Autowired
    private CommonRepository deliveryRepository;

    @Autowired
    private BusinessProperties businessProperties;

    @Override
    @Transactional
    public List<DeviceDTO> getDevice() {
        List<Device> deviceList = deliveryRepository.findByStatusAndHolder();
        List<DeviceDTO> deviceDTOList = new ArrayList<>();

        for (Device device : deviceList) {
            DeviceDTO deviceDTO = new DeviceDTO();
            deviceDTO.setExpirationDate(device.getExpirationDate());
            deviceDTO.setDeviceId(device.getDeviceId());
            if (businessProperties.isTruckingOn()) {
                deviceDTO.setObjectId(device.getDeviceType());
                deviceDTO.setDeviceSize(device.getDeviceDetail());
            } else {
                deviceDTO.setObjectId(device.getDeviceId());
                deviceDTO.setDeviceSize(device.getDeviceDetail());
            }
            deviceDTOList.add(deviceDTO);
        }
        return deviceDTOList;
    }
}
