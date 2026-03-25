package com.MainBusinessLogicDpiModule.MainBusinessLogicDpiModule.Service;

import com.AuthenticationModule.Repository.EnumDeviceDetailRepository;
import com.CommonModule.CommonModule.Entity.EnumDeviceDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class EnumDeviceDetailServiceImpl implements  EnumDeviceDetailService {

    @Autowired
    EnumDeviceDetailRepository enumDeviceDetailRepository;

    @Override
    public List<EnumDeviceDetail> getEnumDeviceDetail() {
        return enumDeviceDetailRepository.findAll();
    }


    @Override
    @Transactional
    public void addEnumDeviceDetail(List<Map<String, Object>> payloadList) {
        for (Map<String, Object> payload : payloadList) {
            EnumDeviceDetail enumDeviceDetail = new EnumDeviceDetail();

            enumDeviceDetail.setDeviceDetailId((String) payload.get("DeviceDetailId"));
            enumDeviceDetail.setDescription((String) payload.get("Description"));


            Optional<EnumDeviceDetail> deviceExisting = enumDeviceDetailRepository.findById(enumDeviceDetail.getDeviceDetailId());
            if (deviceExisting.isPresent()) {
                log.info("Dispositivo con ID {} già contenuto, non verrà inserito", enumDeviceDetail.getDeviceDetailId());
            } else {
                enumDeviceDetailRepository.save(enumDeviceDetail);
                log.info("Dispositivo con ID {} inserito con successo", enumDeviceDetail.getDeviceDetailId());
            }
        }
    }

    @Override
    @Transactional
    public void updateEnumDeviceDetail(List<Map<String, Object>> payloadList) {
        for (Map<String, Object> payload : payloadList) {
            String deviceId =  payload.get("DeviceTypeId").toString();
            Optional<EnumDeviceDetail> existingDevice = enumDeviceDetailRepository.findById(deviceId);

            if (existingDevice.isPresent()) {
                EnumDeviceDetail enumDeviceDetail = existingDevice.get();
                enumDeviceDetail.setDeviceDetailId((String) payload.get("DeviceDetailId"));
                enumDeviceDetail.setDescription((String) payload.get("Description"));

                enumDeviceDetailRepository.save(enumDeviceDetail);
                log.info("Device updated successfully");
            } else {
                log.info("Device not found");
            }
        }
    }

    @Override
    @Transactional
    public void deleteEnumDeviceDetail(List<Map<String, Object>> payloadList) {

        for (Map<String, Object> payload : payloadList) {
            String DeviceTypeId = (String) payload.get("DeviceTypeId");
            Optional<EnumDeviceDetail> existingDevice = enumDeviceDetailRepository.findById(DeviceTypeId);

            if (existingDevice.isPresent()) {
                enumDeviceDetailRepository.delete(existingDevice.get());
                log.info("Device deleted successfully");
            } else {
                log.info("Device not found");
            }
        }
        enumDeviceDetailRepository.flush();
    }
}
