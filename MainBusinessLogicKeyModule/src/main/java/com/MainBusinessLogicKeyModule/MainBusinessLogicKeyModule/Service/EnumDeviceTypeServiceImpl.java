package com.MainBusinessLogicKeyModule.MainBusinessLogicKeyModule.Service;


import com.AuthenticationModule.Repository.EnumDeviceTypeRepository;
import com.CommonModule.CommonModule.Entity.EnumDeviceType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class EnumDeviceTypeServiceImpl implements EnumDeviceTypeService {
    @Autowired
    EnumDeviceTypeRepository enumDeviceTypeRepository;

    @Override
    public List<EnumDeviceType> getEnumDeviceType() {
        return enumDeviceTypeRepository.findAll();
    }

    @Override
    @Transactional
    public void addEnumDeviceType(List<Map<String, Object>> payloadList) {
        for (Map<String, Object> payload : payloadList) {
            EnumDeviceType device = new EnumDeviceType();

            device.setDeviceTypeId((String) payload.get("DeviceTypeId"));
            device.setDeviceVideoName((String) payload.get("DeviceVideoName"));
            device.setTurnBack((String) payload.get("TurnBack"));
            Object imageObj = payload.get("ImageBuffer");
            if (imageObj instanceof List<?> list) {
                byte[] imageBytes = new byte[list.size()];
                for (int i = 0; i < list.size(); i++) {
                    Object val = list.get(i);
                    if (val instanceof Number number) {
                        imageBytes[i] = number.byteValue();
                    } else {
                        throw new IllegalArgumentException("ImageBuffer contiene un valore non numerico");
                    }
                }
                device.setDeviceTypeImage(imageBytes);
            } else if (imageObj == null) {
                device.setDeviceTypeImage(null); // opzionale
            } else {
                throw new IllegalArgumentException("ImageBuffer deve essere una lista di numeri interi");
            }
            device.setAutoResetDay((Integer) payload.get("AutoResetDay"));
            device.setDescription((String) payload.get("Description"));

            Optional<EnumDeviceType> deviceExisting = enumDeviceTypeRepository.findById(device.getDeviceTypeId());
            if (deviceExisting.isPresent()) {
                log.info("Dispositivo con ID {} già contenuto, non verrà inserito", device.getDeviceTypeId());
            } else {
                enumDeviceTypeRepository.save(device);
                log.info("Dispositivo con ID {} inserito con successo", device.getDeviceTypeId());
            }
        }
    }

    @Override
    @Transactional
    public void updateEnumDeviceType(List<Map<String, Object>> payloadList) {
        for (Map<String, Object> payload : payloadList) {
            String deviceId = payload.get("DeviceTypeId").toString();
            Optional<EnumDeviceType> existingDevice = enumDeviceTypeRepository.findById(deviceId);

            if (existingDevice.isPresent()) {
                EnumDeviceType enumDeviceType = existingDevice.get();
                enumDeviceType.setDeviceTypeId((String) payload.get("DeviceTypeId"));
                enumDeviceType.setDeviceVideoName((String) payload.get("DeviceVideoName"));
                enumDeviceType.setTurnBack((String) payload.get("TurnBack"));
                Object imageObj = payload.get("ImageBuffer");
                if (imageObj instanceof List<?> list) {
                    byte[] imageBytes = new byte[list.size()];
                    for (int i = 0; i < list.size(); i++) {
                        Object val = list.get(i);
                        if (val instanceof Number number) {
                            imageBytes[i] = number.byteValue();
                        } else {
                            throw new IllegalArgumentException("ImageBuffer contiene un valore non numerico");
                        }
                    }
                    enumDeviceType.setDeviceTypeImage(imageBytes);
                } else if (imageObj == null) {
                    enumDeviceType.setDeviceTypeImage(null); // opzionale
                } else {
                    throw new IllegalArgumentException("ImageBuffer deve essere una lista di numeri interi");
                }
                enumDeviceType.setAutoResetDay((Integer) payload.get("AutoResetDay"));
                enumDeviceType.setDescription((String) payload.get("Description"));

                enumDeviceTypeRepository.save(enumDeviceType);
                log.info("Device updated successfully");
            } else {
                log.info("Device not found");
            }
        }
    }

    @Override
    @Transactional
    public void deleteEnumDeviceTypes(List<Map<String, Object>> payloadList) {

        for (Map<String, Object> payload : payloadList) {
            String DeviceTypeId = (String) payload.get("DeviceTypeId");
            Optional<EnumDeviceType> existingDevice = enumDeviceTypeRepository.findById(DeviceTypeId);

            if (existingDevice.isPresent()) {
                enumDeviceTypeRepository.delete(existingDevice.get());
                log.info("Device deleted successfully");
            } else {
                log.info("Device not found");
            }
        }
        enumDeviceTypeRepository.flush();

    }

}
