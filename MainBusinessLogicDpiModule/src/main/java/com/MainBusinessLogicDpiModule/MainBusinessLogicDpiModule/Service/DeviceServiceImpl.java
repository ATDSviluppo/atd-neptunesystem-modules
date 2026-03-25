package com.MainBusinessLogicDpiModule.MainBusinessLogicDpiModule.Service;


import com.AuthenticationModule.Repository.DeviceRepository;
import com.AuthenticationModule.Repository.EnumDeviceDetailRepository;
import com.AuthenticationModule.Repository.EnumDeviceTypeRepository;
import com.CommonModule.CommonModule.Entity.Device;
import com.MainBusinessLogicDpiModule.MainBusinessLogicDpiModule.Repository.DeliveryRepository;
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

    @Autowired
    private EnumDeviceTypeRepository enumDeviceTypeRepository;

    @Autowired
    private EnumDeviceDetailRepository enumDeviceDetailRepository;

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Override
    public List<Device> getDevices() {
        return deviceRepository.findAll();
    }

    @Override
    @Transactional
    public ResponseEntity<String> addDevice(Object payloadList) {
        if (payloadList instanceof List) {
            List<Map<String, Object>> payload = (List<Map<String, Object>>) payloadList;

            int numeroUtenti = ((List<?>) payloadList).size();
            int utentiAggiunti = 0;

            for (Map<String, Object> singlePayload : payload) {
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
                    utentiAggiunti++;

                }
                return ResponseEntity.ok("Importazione di " + utentiAggiunti + " su " + numeroUtenti);
            }
        } else if (payloadList instanceof Map) {
            Map<String, Object> singlePayload = (Map<String, Object>) payloadList;
            Device device = new Device();

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
                log.info("Dispositivo già contenuto, non verrà inserito");
                return ResponseEntity.status(500).body("Dispositivo già contenuto, non verrà inserito") ;

            } else {
                deviceRepository.save(device);
                log.info("Dispositivo con ID {} inserito con successo", device.getDeviceId());
                return ResponseEntity.ok("Dispositivo inserito con successo");

            }
        }
        return ResponseEntity.status(500).body("Errore nell'invio dati") ;
    }

    @Override
    @Transactional
    public ResponseEntity<String> updateDevice(List<Map<String, Object>> payloadList) {
        for (Map<String, Object> payload : payloadList) {
            String deviceId =  payload.get("DeviceId").toString();
            Optional<Device> existingDevice = deviceRepository.findById(deviceId);

            if (existingDevice.isPresent()) {
                Device device = existingDevice.get();
                device.setDrumId((Integer) payload.get("DrumId"));
                device.setSectorId((Integer) payload.get("SectorId"));
                device.setExpirationDate((String) payload.get("ExpirationDate"));
                device.setHolder((Boolean) payload.get("Holder"));
                device.setMachineId((String) payload.get("MachineId"));
                device.setLocation((String) payload.get("Location"));

                deviceRepository.save(device);
                log.info("Device updated successfully");
                return ResponseEntity.ok("Aggiornamento avvenuto con successo");
            } else {
                addDevice(payload);
                log.info("Device not found");
                return ResponseEntity.ok("Dispositivo non trovato");
            }
        }
        return null;
    }

    @Override
    @Transactional
    public void deleteDevice(List<Map<String, Object>> payloadList) {
        for (Map<String, Object> payload : payloadList) {
            String deviceId = (String) payload.get("DeviceId");
            Optional<Device> existingDevice = deviceRepository.findById(deviceId);

            if (existingDevice.isPresent()) {
                deviceRepository.delete(existingDevice.get());
                log.info("Device deleted successfully");
            } else {
                log.info("Device not found");
            }
        }
    }

    @Override
    @Transactional
    public void deleteAllDevice() {
        deviceRepository.deleteAll();
    }

}
