package com.MainBusinessLogicDpiModule.MainBusinessLogicDpiModule.Service;

import com.AuthenticationModule.Configuration.LatchManager;
import com.AuthenticationModule.Repository.*;
import com.AuthenticationModule.Service.AuthenticationService;
import com.AuthenticationModule.Utility.OperationMode;
import com.CommonModule.CommonModule.DTO.*;
import com.CommonModule.CommonModule.Entity.*;
import com.CommonModule.CommonModule.Properties.BusinessProperties;
import com.CommonModule.CommonModule.Repository.CommonRepository;
import com.CommonModule.CommonModule.Service.CommonService;
import com.HMIModule.Response.SocketResponse;
import com.HardwareManagerModule.HardwareManagerModule.Latch.MachineLatchManager;
import com.HardwareManagerModule.HardwareManagerModule.Properties.MachineProperties;
import com.HardwareManagerModule.HardwareManagerModule.Service.MachineCommandService;
import com.MainBusinessLogicDpiModule.MainBusinessLogicDpiModule.Entity.EmployeeChoice;
import com.MainBusinessLogicDpiModule.MainBusinessLogicDpiModule.Mapper.DeviceMapper;
import com.MainBusinessLogicDpiModule.MainBusinessLogicDpiModule.Repository.DeliveryRepository;
import com.MainBusinessLogicDpiModule.MainBusinessLogicDpiModule.Repository.EmployeeChoiceRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.AuthenticationModule.Utility.OperationMode.setMode;

@Service
@Slf4j
public class DeliveryServiceImpl implements CommonService, DeliveryService {
    @Autowired
    DeviceRepository deviceRepository;

    @Autowired
    CommonRepository commonRepository;

    @Autowired
    @Lazy
    AuthenticationService authenticationService;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    EmployeeChoiceRepository employeeChoiceRepository;

    @Autowired
    EnumDeviceTypeRepository enumDeviceTypeRepository;

    @Autowired
    EnumDeviceDetailRepository enumDeviceDetailRepository;

    @Autowired
    MachineCommandService machineCommandService;

    @Autowired
    MachineProperties machineProperties;

    @Autowired
    BusinessProperties businessProperties;

    @Autowired
    DeliveryRepository deliveryRepository;

    @Autowired
    SocketResponse socketResponse;

    @Autowired
    MachineLatchManager machineLatchManager;

    @Autowired
    LatchManager badgeLatchManager;

    @Setter
    @Getter
    private Device deviceToTurnBack;


    @Override
    @Transactional
    public UserChoiceDTO analizeUserChoice(Employee employee) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        if (authenticationService.isUserAuthenticate()) {
            if (employee != null) {
                UserChoiceDTO userChoiceDTO = new UserChoiceDTO();
                EmployeeDTO employeeDTO = DeviceMapper.setEmployeeDTO(employee);
                List<DeviceDetailDTO> deviceDetailDTOList = new ArrayList<>();
                List<DeviceTypeDTO> deviceTypeDTOList = new ArrayList<>();
                socketResponse.sendOperationResponse("Waiting", "Elaborazione in corso", objectMapper);
                switch (employee.getEmployeeRole()) {
                    case "User" -> {
                        List<EmployeeChoice> choices = employeeChoiceRepository.findAllByEmployeeId(employee.getEmployeeId());
                        List<DeviceDTO> deviceDTOList = new ArrayList<>();

                        if (!choices.isEmpty()) {
                            for (EmployeeChoice ec : choices) {
                                List<Device> devices = deliveryRepository.findByEnumDeviceTypeIdAndStatusAndHolder(ec.getDeviceTypeId(), true);
                                if (!devices.isEmpty()) {
                                    List<String> detailIds = devices.stream().map(Device::getDeviceDetail).toList();
                                    List<EnumDeviceDetail> enumDeviceDetailList = enumDeviceDetailRepository.findAllById(detailIds);
                                    for (EnumDeviceDetail detail : enumDeviceDetailList) {
                                        int qta = deliveryRepository.countByTypeAndDetailAndHolder(ec.getDeviceTypeId(), detail.getDeviceDetailId(), true);
                                        DeviceDetailDTO deviceDetailDTO = DeviceMapper.setDetailDTO(detail, qta);
                                        deviceDetailDTOList.add(deviceDetailDTO);
                                    }
                                    if (!detailIds.isEmpty()) {
                                        DeviceDTO deviceDTO = DeviceMapper.setDeviceDTO(devices.getFirst(), new ArrayList<>(deviceDetailDTOList));
                                        deviceDTOList.add(deviceDTO);
                                    }
                                }
                                Optional<EnumDeviceType> enumDeviceType = enumDeviceTypeRepository.findById(ec.getDeviceTypeId());
                                if (enumDeviceType.isPresent()) {
                                    EnumDeviceType deviceType = enumDeviceType.get();
                                    DeviceTypeDTO deviceTypeDTO = DeviceMapper.setEnumDeviceTypeDTO(deviceType);
                                    deviceTypeDTOList.add(deviceTypeDTO);
                                }
                                deviceDetailDTOList.clear();
                            }

                            userChoiceDTO = DeviceMapper.getUserChoiceDTO(deviceTypeDTOList, deviceDTOList, employeeDTO);
                            socketResponse.sendUserChoiceToSocket(userChoiceDTO, objectMapper);
                        } else {
                            socketResponse.sendOperationResponse("Failed", "Nessun dispositivo disponibile", objectMapper);
                        }
                    }
                    case "Assistant" -> {
                        List<com.CommonModule.CommonModule.Entity.Device> allDevices = commonRepository.findByHolder(true);

                        Map<String, Map<String, List<Device>>> byTypeThenDetail =
                                allDevices.stream().collect(Collectors.groupingBy(
                                        Device::getDeviceType,
                                        Collectors.groupingBy(Device::getDeviceDetail)
                                ));

                        Map<String, Map<String, Long>> deliveryCounts = new HashMap<>();
                        for (Object[] r : deliveryRepository.countByTypeAndDetail(true)) {
                            String type = (String) r[0];
                            String detail = (String) r[1];
                            Long count = ((Number) r[2]).longValue();

                            deliveryCounts
                                    .computeIfAbsent(type, k -> new HashMap<>())
                                    .put(detail, count);
                        }

                        Set<String> typeIds = byTypeThenDetail.keySet();
                        Map<String, EnumDeviceType> typesById =
                                enumDeviceTypeRepository.findAllById(typeIds).stream()
                                        .collect(Collectors.toMap(EnumDeviceType::getDeviceTypeId, Function.identity()));

                        Set<String> detailIds = byTypeThenDetail.values().stream()
                                .flatMap(m -> m.keySet().stream())
                                .collect(Collectors.toSet());

                        Map<String, EnumDeviceDetail> detailsById =
                                enumDeviceDetailRepository.findAllById(detailIds).stream()
                                        .collect(Collectors.toMap(EnumDeviceDetail::getDeviceDetailId, Function.identity()));

                        List<DeviceDTO> deviceDTOList = new ArrayList<>();
                        List<DeviceTypeDTO> dtoList = new ArrayList<>();

                        for (var typeEntry : byTypeThenDetail.entrySet()) {
                            String typeId = typeEntry.getKey();
                            EnumDeviceType typeEnum = typesById.get(typeId);
                            if (typeEnum == null) continue;

                            dtoList.add(DeviceMapper.setEnumDeviceTypeDTO(typeEnum));

                            Map<String, List<Device>> byDetail = typeEntry.getValue();
                            Set<DeviceDetailDTO> taglie = new HashSet<>();

                            for (var detailEntry : byDetail.entrySet()) {
                                String detailId = detailEntry.getKey();
                                EnumDeviceDetail detailEnum = detailsById.get(detailId);
                                if (detailEnum == null) continue;

                                long cnt = deliveryCounts.getOrDefault(typeId, Map.of())
                                        .getOrDefault(detailId, 0L);

                                taglie.add(DeviceMapper.setDetailDTO(detailEnum, (int) cnt));
                            }

                            Device representative = byDetail.values().iterator().next().getFirst();
                            deviceDTOList.add(DeviceMapper.setDeviceDTO(representative, new ArrayList<>(taglie)));
                        }

                        userChoiceDTO = DeviceMapper.getUserChoiceDTO(dtoList, deviceDTOList, employeeDTO);
                        socketResponse.sendUserChoiceToSocket(userChoiceDTO, objectMapper);

                    }
                    case "Charger" -> {
                        List<Device> deviceList = commonRepository.findByHolder(false);

                        Map<String, Map<String, List<Device>>> byTypeThenDetail =
                                deviceList.stream().collect(Collectors.groupingBy(
                                        Device::getDeviceType,
                                        Collectors.groupingBy(Device::getDeviceDetail)
                                ));

                        Map<String, Map<String, Long>> deliveryCounts = new HashMap<>();
                        for (Object[] r : deliveryRepository.countByTypeAndDetail(false)) {
                            String type = (String) r[0];
                            String detail = (String) r[1];
                            Long count = ((Number) r[2]).longValue();

                            deliveryCounts
                                    .computeIfAbsent(type, k -> new HashMap<>())
                                    .put(detail, count);
                        }

                        Set<String> typeIds = byTypeThenDetail.keySet();
                        Map<String, EnumDeviceType> typesById =
                                enumDeviceTypeRepository.findAllById(typeIds).stream()
                                        .collect(Collectors.toMap(EnumDeviceType::getDeviceTypeId, Function.identity()));

                        Set<String> detailIds = byTypeThenDetail.values().stream()
                                .flatMap(m -> m.keySet().stream())
                                .collect(Collectors.toSet());

                        Map<String, EnumDeviceDetail> detailsById =
                                enumDeviceDetailRepository.findAllById(detailIds).stream()
                                        .collect(Collectors.toMap(EnumDeviceDetail::getDeviceDetailId, Function.identity()));

                        List<DeviceDTO> deviceDTOList = new ArrayList<>();
                        List<DeviceTypeDTO> dtoList = new ArrayList<>();

                        for (var typeEntry : byTypeThenDetail.entrySet()) {
                            String typeId = typeEntry.getKey();
                            EnumDeviceType typeEnum = typesById.get(typeId);
                            if (typeEnum == null) continue;

                            dtoList.add(DeviceMapper.setEnumDeviceTypeDTO(typeEnum));

                            Map<String, List<Device>> byDetail = typeEntry.getValue();
                            Set<DeviceDetailDTO> taglie = new HashSet<>();

                            for (var detailEntry : byDetail.entrySet()) {
                                String detailId = detailEntry.getKey();
                                EnumDeviceDetail detailEnum = detailsById.get(detailId);
                                if (detailEnum == null) continue;

                                long cnt = deliveryCounts.getOrDefault(typeId, Map.of())
                                        .getOrDefault(detailId, 0L);

                                taglie.add(DeviceMapper.setDetailDTO(detailEnum, (int) cnt));
                            }

                            Device representative = byDetail.values().iterator().next().getFirst();
                            deviceDTOList.add(DeviceMapper.setDeviceDTO(representative, new ArrayList<>(taglie)));
                        }

                        userChoiceDTO = DeviceMapper.getUserChoiceDTO(dtoList, deviceDTOList, employeeDTO);
                        socketResponse.sendUserChoiceToSocket(userChoiceDTO, objectMapper);
                    }

                }
                return userChoiceDTO;
            } else {
                log.error("Nessun employee da analizzare");
            }
            return null;
        } else
            socketResponse.sendOperationResponse("Failed", "Autenticazione fallita per l'utente: " + employee.getEmployeeId(), objectMapper);
        return null;
    }

    @Override
    @Transactional
    public ResponseEntity<String> analizeUserRetreat(Map<String, Object> payload) throws
            Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        String deviceId = (String) payload.get("deviceId");
        String employeeId = (String) payload.get("employeeId");
        String deviceDetailId = (String) payload.get("deviceDetailId");
        boolean isAssistant = (boolean) payload.get("isAssistant");


        log.info("deviceId" + payload.get("deviceId") +
                "employeeId" + payload.get("employeeId") +
                "isAssistant" + payload.get("isAssistant") +
                "deviceDetailId" + payload.get("deviceDetailId"));

        Date eventTimestamp = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SS");
        sdf.format(eventTimestamp);

        Employee employee = employeeRepository.getReferenceById(employeeId);

        if (employee.getEmployeeRole().equals("Charger")) {
            log.info("qta: " + payload.get("qta"));
            int qta = (int) payload.get("qta");
            List<Device> deviceList = deliveryRepository.findByDeviceTypeAndDeviceDetailAndHolder(deviceId, deviceDetailId, false);

            if (deviceList.size() < qta) {
                socketResponse.sendOperationResponse("Failed", "Numero di dispositivi non configurato nel database", objectMapper);
                return ResponseEntity.status(500).body("Operazione non riuscita");
            }

            deviceList = deviceList.subList(0, qta);

            for (Device d : deviceList) {
                deliveryRepository.updateDeviceCharged(d.getDeviceId(), String.valueOf(UUID.randomUUID()));
                Event event = new Event(eventTimestamp, "Load", d.getDeviceId(), employeeId, d.getDeviceDetail(), d.getDeviceType(), d.getLocation(), d.getMachineId(), "", true);
                eventRepository.save(event);
            }

            socketResponse.sendOperationResponse("Success", "Carico eseguito con successo", objectMapper);
            return ResponseEntity.ok("Operazione completata per dispositivo ");
        }

        Device device;
        List<Device> deviceList = deliveryRepository.findByDeviceTypeAndDeviceDetailAndHolder(deviceId, deviceDetailId, true);
        if (deviceList.isEmpty()) {
            socketResponse.sendOperationResponse("Failed", "Numero di dispositivi non configurato nel database", objectMapper);
            return ResponseEntity.status(500).body("Operazione non riuscita");
        }
        device = deviceList.getFirst();

        if (!device.getHolder()) {
            if (!isAssistant) {
                socketResponse.sendOperationResponse("Failed", "Dispositivo non contenuto all'interno", objectMapper);
                return ResponseEntity.status(500).body("Operazione non riuscita");
            }
        }

        if (businessProperties.getMachineId().contains("S2") || businessProperties.getMachineId().contains("MDS")) {
            HttpStatus response = machineCommandService.stopListening();
        }

        // Prima fase: Posizionamento
        if (businessProperties.getMachineId().contains("900")) {
            machineLatchManager.createSectorLatch();
            machineCommandService.positionMachineDoor(device.getDrumId().toString(), device.getSectorId().toString());
            socketResponse.sendOperationResponse("Waiting", "Posizionamento in corso", objectMapper);

            if (awaitLatch(machineLatchManager.getSectorLatch(), 45, "Timeout waiting for sector positioning")) {
                return ResponseEntity.status(500).body("Operazione non riuscita");
            }

            machineLatchManager.createOpenLatch();
        }

        HttpStatus response = machineCommandService.openMachineDoor(device.getDrumId().toString(), device.getSectorId().toString());

        if (businessProperties.getMachineId().contains("900")) {
            if (awaitLatch(machineLatchManager.getOpenLatch(), 45, "Timeout waiting for door opening")) {
                return ResponseEntity.status(500).body("Operazione non riuscita");
            }
        }
        DeviceDTO deviceDTO = DeviceMapper.setDeviceDTO(device, null);

        socketResponse.sendRetreatResponse("Success", "Ritira il dispositivo", deviceDTO, objectMapper);

        boolean successUpdateDevice = updateDeviceOut(device.getDeviceId(), employeeId);

        if (!successUpdateDevice) {
            log.info("Non è stato possibile aggiornare il device nel database");
        }

        String note = isAssistant ? "Prelievo effettuato da assistente" : "";

        Event event = new Event(eventTimestamp, "Retreat", device.getDeviceId(), employeeId, device.getDeviceDetail(), device.getDeviceType(), device.getLocation(), device.getMachineId(), note, true);
        eventRepository.save(event);


        log.info("Operazione completata con successo per il dispositivo ID: {}", device.getDeviceId());

        if (businessProperties.getMachineId().contains("S2") || businessProperties.getMachineId().contains("MDS")) {
            machineCommandService.startListening();
        }

        return ResponseEntity.status(200).body("Operazione riuscita");
    }

    @Override
    @Transactional
    public boolean analizeUserTurnBack(String deviceGuid) throws JsonProcessingException, InterruptedException {
        Device device = deviceRepository.findByEpcCode(deviceGuid);
        ObjectMapper objectMapper = new ObjectMapper();

        if (device == null) {
            log.info("Dispositivo non trovato per il GUID fornito: " + deviceGuid);
            return false;
        }

        if (device.getHolder()) {
            socketResponse.sendOperationResponse("Failed", "Dispositivo già contenuto all'interno", objectMapper);
            return false;
        }

        setMode(OperationMode.TURNBACK);
        socketResponse.sendOperationResponse("Success", "Adesso passa il badge", objectMapper);

        badgeLatchManager.createBadgeScanLatch();
        setDeviceToTurnBack(device);

        if (!awaitLatch(badgeLatchManager.getBadgeScanLatch(), 30, "Badge scan")) {
            socketResponse.sendOperationResponse("Failed", "Timeout per il passaggio del badge scaduto", objectMapper);
            return false;
        }

        Employee employee = employeeRepository.findByEmployeeCard(authenticationService.getBadgeCode());

        if (!isAuthorizedToTurnBack(employee, device)) {
            assert employee != null;
            socketResponse.sendOperationResponse("Failed", "Non è possibile riconsegnare con questo badge", objectMapper);
            return false;
        }

        if (businessProperties.getMachineId().contains("S2") || businessProperties.getMachineId().contains("MDS")) {
            HttpStatus response = machineCommandService.stopListening();
        }

        // Prima fase: Posizionamento
        if (businessProperties.getMachineId().contains("900")) {
            machineLatchManager.createSectorLatch();
            machineCommandService.positionMachineDoor(device.getDrumId().toString(), device.getSectorId().toString());
            socketResponse.sendOperationResponse("Waiting", "Posizionamento in corso", objectMapper);

            if (awaitLatch(machineLatchManager.getSectorLatch(), 45, "Timeout waiting for sector positioning")) {
                return false;
            }

            machineLatchManager.createOpenLatch();
        }

        HttpStatus response = machineCommandService.openMachineDoor(device.getDrumId().toString(), device.getSectorId().toString());

        if (businessProperties.getMachineId().contains("900")) {
            if (awaitLatch(machineLatchManager.getOpenLatch(), 45, "Timeout waiting for door opening")) {
                return false;
            }
        }

        DeviceDTO deviceDTO = DeviceMapper.setDeviceDTO(device, null);

        socketResponse.sendTurnBackResponse("Success", "Riconsegna il dispositivo", deviceDTO, objectMapper);
        log.info("deviceID: " + device.getDeviceId());

        String note = "?";
        Date eventTimestamp = new Date();

        if (employee.getEmployeeRole().equals("Assistant")) {
            note = "Riconsegna effettuata da assistente";
        }

        if (!updateDeviceOn(device.getDeviceId())) {
            log.info("Non è stato possibile aggiornare il dispositivo");
        }

        Event event = new Event(eventTimestamp, "TurnBack", device.getDeviceId(), employee.getEmployeeId(), device.getDeviceDetail(), device.getDeviceType(), device.getLocation(), device.getMachineId(), note, true);
        eventRepository.save(event);

        if (businessProperties.getMachineId().contains("S2") || businessProperties.getMachineId().contains("MDS")) {
            machineCommandService.startListening();
        }

        return true;
    }

    @Override
    public boolean analizeUserCharge(String deviceGuid) throws JsonProcessingException, InterruptedException {
        Device device = deviceRepository.findByEpcCode(deviceGuid);
        ObjectMapper objectMapper = new ObjectMapper();

        if (device == null) {
            log.info("Dispositivo non trovato per il GUID fornito: " + deviceGuid);
            socketResponse.sendOperationResponse("Failed", "Dispositivo non esistente", objectMapper);
            return false;
        }

        String note = "?";
        Date eventTimestamp = new Date();

        Event event = new Event(eventTimestamp, "Load", device.getDeviceId(), "", device.getDeviceDetail(), device.getDeviceType(), device.getLocation(), device.getMachineId(), note, true);
        eventRepository.save(event);

        // Prima fase: Posizionamento
        machineLatchManager.createSectorLatch();
        machineCommandService.positionMachineDoor(device.getDrumId().toString(), device.getSectorId().toString());
        socketResponse.sendOperationResponse("Waiting", "Posizionamento in corso", objectMapper);

        if (awaitLatch(machineLatchManager.getSectorLatch(), 45, "Timeout waiting for sector positioning")) {
            return false;
        }

        machineLatchManager.createOpenLatch();
        HttpStatus response = machineCommandService.openMachineDoor(device.getDrumId().toString(), device.getSectorId().toString());

        socketResponse.sendChargeResponse("Success", "Carica il dispositivo", objectMapper);

        if (awaitLatch(machineLatchManager.getOpenLatch(), 45, "Timeout waiting for door opening")) {
            return false;
        }

        Thread.sleep(machineProperties.getTimeCloseDoor());

        machineLatchManager.createCloseLatch();
        machineCommandService.closeMachineDoor(device.getDrumId().toString(), device.getSectorId().toString());

        if (awaitLatch(machineLatchManager.getCloseLatch(), 45, "Timeout waiting for door closing")) {
            return false;
        }

        return true;
    }

    @Override
    public boolean analizeMassiveCharger() throws JsonProcessingException {
        List<Device> deviceList = commonRepository.findByHolder(false);
        ObjectMapper objectMapper = new ObjectMapper();

        for (Device d : deviceList) {
            Date eventTimestamp = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SS");
            sdf.format(eventTimestamp);
            Event event = new Event(eventTimestamp, "Load", d.getDeviceId(), "", d.getDeviceDetail(), d.getDeviceType(), d.getLocation(), d.getMachineId(), "", true);
            eventRepository.save(event);

            deliveryRepository.updateDeviceCharged(d.getDeviceId(), generateGuid());
        }
        socketResponse.sendOperationResponse("Success", "Carico eseguito con successo", objectMapper);

        return true;
    }

    @Override
    public List<EmployeeDTO> getEmployeeForAssistantRetreat() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            List<Employee> employeeList = employeeRepository.findAll();
            List<EmployeeDTO> employeeDTOList = employeeList.stream().map(DeviceMapper::setEmployeeDTO).toList();
            socketResponse.sendEmployeeToSocket(employeeList, objectMapper);
            log.debug(employeeList.toString());
            return employeeDTOList;
        } catch (Exception e) {
            log.error("Error occurred while retrieving employees for assistant retreat", e);
            return Collections.emptyList();
        }
    }

    @Override
    public boolean updateDeviceOn(String deviceId) {
        try {
            int updatedCount = commonRepository.updateDeviceOn(deviceId);
            return updatedCount > 0;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateDeviceOut(String deviceId, String employeeId) {
        try {
            int updatedCount = commonRepository.updateDeviceOut(deviceId, employeeId);
            return updatedCount > 0;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    private boolean isAuthorizedToTurnBack(Employee employee, Device device) {
        if (employee == null) return false;
        return Objects.equals(employee.getEmployeeId(), device.getTemporaryOwner()) ||
                Objects.equals(employee.getEmployeeRole(), "Assistant");
    }

    public boolean awaitLatch(CountDownLatch latch, int timeoutSeconds, String operationName) throws
            InterruptedException {
        boolean result = latch.await(timeoutSeconds, TimeUnit.SECONDS);
        if (!result) {
            log.warn(operationName + " non completato entro il timeout di " + timeoutSeconds + " secondi.");
        }
        return result;
    }

    public static String generateGuid() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replace("-", "");
    }


}
