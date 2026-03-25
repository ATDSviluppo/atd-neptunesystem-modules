package com.MainBusinessLogicKeyModule.MainBusinessLogicKeyModule.Service;


import com.AuthenticationModule.Repository.DeviceRepository;
import com.CommonModule.CommonModule.Entity.Device;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class DeviceServiceImpl implements DeviceService {
    @Autowired
    private DeviceRepository deviceRepository;

    @Override
    public List<Device> getDevices() {
        return deviceRepository.findAll();
    }

    @Override
    @Transactional
    @SuppressWarnings("unchecked")
    public void addDevice(Object payloadList) {
        if (payloadList instanceof List) {

            List<Map<String, Object>> payload = (List<Map<String, Object>>) payloadList;

            for (Map<String, Object> singlePayload : payload) {
                Device device = new Device();
                log.info(payload.toString());

                device.setDeviceId((String) singlePayload.get("DeviceId"));
                device.setDrumId(singlePayload.get("DrumId") != null ? Integer.valueOf(singlePayload.get("DrumId").toString()) : null);
                device.setSectorId(singlePayload.get("SectorId") != null ? Integer.valueOf(singlePayload.get("SectorId").toString()) : null);
                device.setExpirationDate((String) singlePayload.get("ExpirationDate"));
                device.setNominalNumber(singlePayload.get("NominalNumber") != null ? Integer.valueOf(singlePayload.get("NominalNumber").toString()) : null);
                device.setHolder(singlePayload.get("Holder") != null ? Boolean.valueOf(singlePayload.get("Holder").toString()) : null);
                device.setTemporaryOwner((String) singlePayload.get("TemporaryOwner"));
                device.setEcpCode((String) singlePayload.get("EcpCode"));
                device.setStatus((String) singlePayload.get("Status"));
                device.setDeviceBarCode((String) singlePayload.get("DeviceBarCode"));
                device.setMachineId((String) singlePayload.get("MachineId"));
                device.setLocation((String) singlePayload.get("Location"));
                device.setDeviceType((String) singlePayload.get("DeviceTypeId"));
                device.setDeviceDetail((String) singlePayload.get("DeviceDetailId"));


                Optional<Device> deviceExisting = deviceRepository.findById(device.getDeviceId());
                if (deviceExisting.isPresent()) {
                    log.info("Dispositivo con ID {} già contenuto, non verrà inserito", device.getDeviceId());
                } else {
                    deviceRepository.save(device);
                    log.info("Dispositivo con ID {} inserito con successo", device.getDeviceId());
                }
            }
        } else if (payloadList instanceof Map) {
            Map<String, Object> singlePayload = (Map<String, Object>) payloadList;
            Device device = new Device();
            log.info(singlePayload.toString());

            device.setDeviceId((String) singlePayload.get("DeviceId"));
            device.setDrumId(singlePayload.get("DrumId") != null ? Integer.valueOf(singlePayload.get("DrumId").toString()) : null);
            device.setSectorId(singlePayload.get("SectorId") != null ? Integer.valueOf(singlePayload.get("SectorId").toString()) : null);
            device.setExpirationDate((String) singlePayload.get("ExpirationDate"));
            device.setNominalNumber(singlePayload.get("NominalNumber") != null ? Integer.valueOf(singlePayload.get("NominalNumber").toString()) : null);
            device.setHolder(singlePayload.get("Holder") != null ? Boolean.valueOf(singlePayload.get("Holder").toString()) : null);
            device.setTemporaryOwner((String) singlePayload.get("TemporaryOwner"));
            device.setEcpCode((String) singlePayload.get("EcpCode"));
            device.setStatus((String) singlePayload.get("Status"));
            device.setDeviceBarCode((String) singlePayload.get("DeviceBarCode"));
            device.setMachineId((String) singlePayload.get("MachineId"));
            device.setLocation((String) singlePayload.get("Location"));
            device.setDeviceType((String) singlePayload.get("DeviceTypeId"));
            device.setDeviceDetail((String) singlePayload.get("DeviceDetailId"));

            Optional<Device> deviceExisting = deviceRepository.findById(device.getDeviceId());
            if (deviceExisting.isPresent()) {
                log.info("Dispositivo con ID {} già contenuto, non verrà inserito", device.getDeviceId());
            } else {
                deviceRepository.save(device);
                log.info("Dispositivo con ID {} inserito con successo", device.getDeviceId());
            }
        }
    }

    @Override
    @Transactional
    public ResponseEntity<String> updateDevice(Object payloadList) {
        if (payloadList instanceof List) {
            List<Map<String, Object>> payload = (List<Map<String, Object>>) payloadList;
            for (Map<String, Object> singlePayload : payload) {

                String deviceId = singlePayload.get("DeviceId").toString();
                Optional<Device> existingDevice = deviceRepository.findById(deviceId);

                if (existingDevice.isPresent()) {
                    Device device = existingDevice.get();
                    device.setDrumId((Integer) singlePayload.get("DrumId"));
                    device.setSectorId((Integer) singlePayload.get("SectorId"));
                    //device.setEcpCode((String) payload.get("EcpCode"));
                    device.setHolder(singlePayload.get("Holder") != null ? Boolean.valueOf(singlePayload.get("Holder").toString()) : null);
                    device.setTemporaryOwner((String) singlePayload.get("TemporaryOwner"));

                    deviceRepository.save(device);
                    deviceRepository.flush();
                    log.info("Device updated successfully");
                    return ResponseEntity.ok("Aggiornamento avvenuto con successo");
                } else {
                    return ResponseEntity.ok("Dispositivo non trovato");
                }
            }

        } else if (payloadList instanceof Map) {
            Map<String, Object> singlePayload = (Map<String, Object>) payloadList;

            String deviceId = singlePayload.get("DeviceId").toString();
            Optional<Device> existingDevice = deviceRepository.findById(deviceId);

            if (existingDevice.isPresent()) {
                Device device = existingDevice.get();
                device.setDrumId((Integer) singlePayload.get("DrumId"));
                device.setSectorId((Integer) singlePayload.get("SectorId"));
                //device.setEcpCode((String) payload.get("EcpCode"));
                device.setHolder(singlePayload.get("Holder") != null ? Boolean.valueOf(singlePayload.get("Holder").toString()) : null);
                device.setTemporaryOwner((String) singlePayload.get("TemporaryOwner"));

                deviceRepository.save(device);
                deviceRepository.flush();
                log.info("Device updated successfully");
                return ResponseEntity.ok("Aggiornamento avvenuto con successo");
            } else {
                return ResponseEntity.ok("Dispositivo non trovato");
            }
        }
        return ResponseEntity.status(500).body("Errore nella formattazione dati");

    }

    @Override
    @Transactional
    public ResponseEntity<String> deleteDevice(Map<String, Object> payload) {
        String deviceId = (String) payload.get("DeviceId");
        Optional<Device> existingDevice = deviceRepository.findById(deviceId);

        if (existingDevice.isPresent()) {
            deviceRepository.delete(existingDevice.get());
            log.info("Device deleted successfully");
            return ResponseEntity.ok("Dispositivo eliminato con successo");
        } else {
            log.info("Device not found");
            return ResponseEntity.ok("Dispositivo non trovato");
        }
    }

}
