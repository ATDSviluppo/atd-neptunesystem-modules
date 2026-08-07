package com.Mobile.MobileModule.Service;

import com.CommonModule.CommonModule.Entity.Device;
import com.CommonModule.CommonModule.Properties.BusinessProperties;
import com.CommonModule.CommonModule.Repository.CommonRepository;
import com.Mobile.MobileModule.DTO.DeviceDTO;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class MobileServiceImpl implements MobileService {
    @Autowired
    private CommonRepository deliveryRepository;

    @Autowired
    private BusinessProperties businessProperties;

    @Override
    @Transactional
    public List<DeviceDTO> getDevice() {
        List<DeviceDTO> deviceDTOList = new ArrayList<>();
        if (!businessProperties.isTruckingOn()) {
            List<Device> deviceList = deliveryRepository.findByStatusAndHolder();

            for (Device device : deviceList) {
                DeviceDTO deviceDTO = new DeviceDTO();
                deviceDTO.setExpirationDate(device.getExpirationDate());
                deviceDTO.setDeviceId(device.getDeviceId());
                deviceDTO.setObjectId(device.getDeviceId());
                deviceDTO.setDeviceSize(device.getDeviceDetail());
                deviceDTOList.add(deviceDTO);
            }
        } else {
            List<Object[]> deviceList = deliveryRepository.findDetailsAndTypesWhereHolderTrue();


            for (Object[] device : deviceList) {
                DeviceDTO deviceDTO = new DeviceDTO();
                deviceDTO.setDeviceId((String) device[2]);
                deviceDTO.setDeviceSize((String) device[0]);
                deviceDTO.setObjectId((String) device[1]);
                deviceDTO.setDeviceSizeId((String) device[3]);

                deviceDTOList.add(deviceDTO);
            }

        }
        return deviceDTOList;
    }
}
