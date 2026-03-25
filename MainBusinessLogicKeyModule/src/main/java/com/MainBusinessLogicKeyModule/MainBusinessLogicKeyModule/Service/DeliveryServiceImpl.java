package com.MainBusinessLogicKeyModule.MainBusinessLogicKeyModule.Service;

import com.AuthenticationModule.Configuration.LatchManager;
import com.AuthenticationModule.Repository.*;
import com.AuthenticationModule.Service.AuthenticationService;
import com.AuthenticationModule.Utility.OperationMode;
import com.CommonModule.CommonModule.DTO.DeviceDTO;
import com.CommonModule.CommonModule.DTO.DeviceTypeDTO;
import com.CommonModule.CommonModule.DTO.EmployeeDTO;
import com.CommonModule.CommonModule.DTO.UserChoiceDTO;
import com.CommonModule.CommonModule.Entity.Device;
import com.CommonModule.CommonModule.Entity.Employee;
import com.CommonModule.CommonModule.Entity.EnumDeviceType;
import com.CommonModule.CommonModule.Entity.Event;
import com.CommonModule.CommonModule.Properties.BusinessProperties;
import com.CommonModule.CommonModule.Repository.CommonRepository;
import com.CommonModule.CommonModule.Service.CommonService;
import com.HMIModule.Response.SocketResponse;
import com.HardwareManagerModule.HardwareManagerModule.Latch.MachineLatchManager;
import com.HardwareManagerModule.HardwareManagerModule.Properties.MachineProperties;
import com.HardwareManagerModule.HardwareManagerModule.Service.MachineCommandService;
import com.MainBusinessLogicKeyModule.MainBusinessLogicKeyModule.Entity.Planning;
import com.MainBusinessLogicKeyModule.MainBusinessLogicKeyModule.Entity.PlanningRetriedMap;
import com.MainBusinessLogicKeyModule.MainBusinessLogicKeyModule.Mapper.DeviceMapper;
import com.MainBusinessLogicKeyModule.MainBusinessLogicKeyModule.Repository.DeliveryRepository;
import com.MainBusinessLogicKeyModule.MainBusinessLogicKeyModule.Repository.PlanningRepository;
import com.MainBusinessLogicKeyModule.MainBusinessLogicKeyModule.Repository.PlanningRetriedMapRepository;
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

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static com.AuthenticationModule.Utility.OperationMode.setMode;

@Service
@Slf4j
public class DeliveryServiceImpl implements CommonService {
    @Autowired
    DeviceRepository deviceRepository;

    @Autowired
    @Lazy
    AuthenticationService authenticationService;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    PlanningRepository planningRepository;

    @Autowired
    PlanningRetriedMapRepository planningRetriedMapRepository;

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
    CommonRepository commonRepository;

    @Autowired
    PlanningService planningService;

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
                List<DeviceTypeDTO> deviceTypeDTOList = new ArrayList<>();
                List<DeviceDTO> deviceDistinct = new ArrayList<>();
                switch (employee.getEmployeeRole()) {
                    case "User" -> {
                        Planning planning = getFirstActivePlanByEmployeeId(employee.getEmployeeId());
                        if (planning != null) {
                            List<Device> deviceList = deviceRepository.findAllById(Collections.singleton(planning.getDeviceId()));

                            for (Device device : deviceList) {
                                DeviceDTO deviceDTO = DeviceMapper.setDeviceDTO(device);
                                deviceDistinct.add(deviceDTO);
                            }

                            List<EnumDeviceType> enumDeviceTypeList = enumDeviceTypeRepository.findAllById(Collections.singleton(planning.getEnumDeviceType()));

                            for (EnumDeviceType enumDeviceType : enumDeviceTypeList) {
                                DeviceTypeDTO deviceTypeDTO = DeviceMapper.setEnumDeviceTypeDTO(enumDeviceType);
                                deviceTypeDTOList.add(deviceTypeDTO);
                            }

                            EmployeeDTO employeeDTO = DeviceMapper.setEmployeeDTO(employee);

                            userChoiceDTO = DeviceMapper.getUserChoiceDTO(deviceTypeDTOList, deviceDistinct, employeeDTO);

                            socketResponse.sendUserChoiceToSocket(userChoiceDTO, objectMapper);
                        } else {
                            socketResponse.sendOperationResponse("Failed", "Nessun planning presente per l'utente " + employee.getEmployeeName(), objectMapper);
                        }
                    }
                    case "Assistant" -> {
                        List<Device> allDevices = commonRepository.findByHolder(true);
                        List<DeviceTypeDTO> enumDeviceTypeList = new ArrayList<>();
                        List<DeviceDTO> deviceDTOList = new ArrayList<>();

                        List<DeviceDTO> deviceList = commonRepository.findByStatusAndHolder()
                                .stream()
                                .map(DeviceMapper::setDeviceDTO)
                                .toList();

                        EmployeeDTO employeeDTO = DeviceMapper.setEmployeeDTO(employee);

                        userChoiceDTO = DeviceMapper.getUserChoiceDTO(enumDeviceTypeList, deviceList, employeeDTO);
                        socketResponse.sendUserChoiceToSocket(userChoiceDTO, objectMapper);
                    }
                    case "Charger" -> {
                        EmployeeDTO employeeDTO = DeviceMapper.setEmployeeDTO(employee);
                        List<DeviceDTO> deviceDTOList = new ArrayList<>();
                        List<DeviceTypeDTO> enumDeviceTypeDTO = new ArrayList<>();

                        userChoiceDTO = DeviceMapper.getUserChoiceDTO(enumDeviceTypeDTO, deviceDTOList, employeeDTO);
                        socketResponse.sendUserChoiceToSocket(userChoiceDTO, objectMapper);
                    }
                    default -> {
                        log.error("Ruolo utente non supportato: " + employee.getEmployeeRole());
                        return null;
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
        try {

            ObjectMapper objectMapper = new ObjectMapper();

            String deviceId = (String) payload.get("deviceId");
            String employeeId = (String) payload.get("employeeId");
            boolean isAssistant = (boolean) payload.get("isAssistant");

            log.info("deviceId" + payload.get("deviceId") +
                    "employeeId" + payload.get("employeeId") +
                    "isAssistant" + payload.get("isAssistant"));

            Date eventTimestamp = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SS");
            sdf.format(eventTimestamp);

            Device device;

            device = deviceRepository.getReferenceById(deviceId);

            if (!device.getHolder()) {
                if (!isAssistant) {
                    socketResponse.sendOperationResponse("Failed", "Dispositivo non contenuto all'interno", objectMapper);
                    return ResponseEntity.status(500).body("Operazione non riuscita");
                }
            }

            if (businessProperties.getMachineId().contains("S2") || businessProperties.getMachineId().contains("MDS")) {
                machineCommandService.stopListening();
            }

            socketResponse.sendOperationResponse("Waiting", "Posizionamento in corso", objectMapper);

            // Prima fase: Posizionamento
            if (businessProperties.getMachineId().contains("900")) {
                machineLatchManager.createSectorLatch();
                machineCommandService.positionMachineDoor(device.getDrumId().toString(), device.getSectorId().toString());

                if (awaitLatch(machineLatchManager.getSectorLatch(), 45, "Timeout waiting for sector positioning")) {
                    return ResponseEntity.status(500).body("Operazione non riuscita");
                }

                machineLatchManager.createOpenLatch();
            }

            HttpStatus response = machineCommandService.openMachineDoor(device.getDrumId().toString(), device.getSectorId().toString());

            if (response == HttpStatus.INTERNAL_SERVER_ERROR) {
                socketResponse.sendOperationResponse("Failed", "Macchina non connessa, riavvio il dispenser", objectMapper);
                machineCommandService.resetMachineRequest();
                return ResponseEntity.status(500).body("Operazione non riuscita");
            }

            if (businessProperties.getMachineId().contains("900")) {
                if (awaitLatch(machineLatchManager.getOpenLatch(), 45, "Timeout waiting for door opening")) {
                    return ResponseEntity.status(500).body("Operazione non riuscita");
                }
            }

            DeviceDTO deviceDTO = DeviceMapper.setDeviceDTO(device);
            socketResponse.sendRetreatResponse("Success", "Ritira il dispositivo", deviceDTO, objectMapper);

            boolean successUpdateDevice = updateDeviceOut(device.getDeviceId(), employeeId);

            if (!successUpdateDevice) {
                log.info("Non è stato possibile aggiornare il device nel database");
            }

            String note = isAssistant ? "Prelievo effettuato da assistente" : "";

            Planning planning = getFirstActivePlanByEmployeeId(employeeId);

            if (planning == null) {
                Event event = new Event(eventTimestamp, "Retreat", deviceId, employeeId, null, device.getDeviceDetail(), device.getDeviceType(), device.getLocation(), device.getMachineId(), note, false);
                eventRepository.save(event);
                return ResponseEntity.status(200).body("Operazione riuscita");
            }

            Event event = new Event(eventTimestamp, "Retreat", deviceId, employeeId, planning.getPlanningId(), device.getDeviceDetail(), device.getDeviceType(), device.getLocation(), device.getMachineId(), note, true);
            eventRepository.save(event);

            PlanningRetriedMap planningRetriedMap = new PlanningRetriedMap(planning.getPlanningId(), deviceId, employeeId);

            planningRetriedMapRepository.save(planningRetriedMap);
            deletePlanningById(planning.getPlanningId());

            planningRetriedMapRepository.flush();
            planningRepository.flush();

            log.info("Operazione completata con successo per il dispositivo ID: {}", device.getDeviceId());

        } finally {
            if (businessProperties.getMachineId().contains("S2") || businessProperties.getMachineId().contains("MDS")) {
                machineCommandService.startListening();
            }
        }
        return ResponseEntity.status(200).body("Operazione riuscita");
    }

    @Override
    @Transactional
    public boolean analizeUserTurnBack(String deviceGuid) throws IOException, InterruptedException {
        try {
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
            DeviceDTO deviceDTO = DeviceMapper.setDeviceDTO(device);
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
                machineCommandService.stopListening();
            }

            socketResponse.sendOperationResponse("Waiting", "Posizionamento in corso", objectMapper);

            // Prima fase: Posizionamento
            if (businessProperties.getMachineId().contains("900")) {
                machineLatchManager.createSectorLatch();
                machineCommandService.positionMachineDoor(device.getDrumId().toString(), device.getSectorId().toString());

                if (awaitLatch(machineLatchManager.getSectorLatch(), 45, "Timeout waiting for sector positioning")) {
                    return false;
                }

                machineLatchManager.createOpenLatch();
            }

            HttpStatus response = machineCommandService.openMachineDoor(device.getDrumId().toString(), device.getSectorId().toString());

            if (response == HttpStatus.INTERNAL_SERVER_ERROR) {
                socketResponse.sendOperationResponse("Failed", "Macchina non connessa, riavvio il dispenser", objectMapper);
                machineCommandService.resetMachineRequest();
                return false;
            }

            if (businessProperties.getMachineId().contains("900")) {
                if (awaitLatch(machineLatchManager.getOpenLatch(), 45, "Timeout waiting for door opening")) {
                    return false;
                }
            }

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

            log.info("DeviceId: " + device.getDeviceId());
            List<PlanningRetriedMap> planningRetriedMapList = planningRetriedMapRepository.findByDeviceId(device.getDeviceId());
            if (planningRetriedMapList.isEmpty()) {
                Event event = new Event(eventTimestamp, "TurnBack", device.getDeviceId(), employee.getEmployeeId(), null, device.getDeviceDetail(), device.getDeviceType(), device.getLocation(), device.getMachineId(), note, false);
                eventRepository.save(event);
                return true;
            }
            PlanningRetriedMap planningRetriedMap = planningRetriedMapList.getLast();

            Event event = new Event(eventTimestamp, "TurnBack", device.getDeviceId(), employee.getEmployeeId(), planningRetriedMap.getPlanningId(), device.getDeviceDetail(), device.getDeviceType(), device.getLocation(), device.getMachineId(), note, true);
            eventRepository.save(event);
            planningRetriedMapRepository.delete(planningRetriedMap);
            planningRetriedMapRepository.flush();

            Thread.sleep(machineProperties.getTimeCloseDoor());
            if (businessProperties.getMachineId().contains("900")) {
                machineLatchManager.createCloseLatch();

                if (awaitLatch(machineLatchManager.getCloseLatch(), 45, "Timeout waiting for door closing")) {
                    return false;
                }
            }

            //machineCommandService.closeMachineDoor(device.getDrumId().toString(), device.getSectorId().toString());

        } finally {
            if (businessProperties.getMachineId().contains("S2") || businessProperties.getMachineId().contains("MDS")) {
                machineCommandService.startListening();
            }
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

        Event event = new Event(eventTimestamp, "Load", device.getDeviceId(), "", device.getDeviceDetail(), device.getDeviceType(), device.getLocation(), device.getMachineId(), note, false);
        eventRepository.save(event);

        socketResponse.sendOperationResponse("Waiting", "Posizionamento in corso", objectMapper);

        if (!businessProperties.getMachineId().contains("MDS")) {
            // Prima fase: Posizionamento
            if (!businessProperties.getMachineId().contains("S2")) {
                machineLatchManager.createSectorLatch();
                machineCommandService.positionMachineDoor(device.getDrumId().toString(), device.getSectorId().toString());

                if (awaitLatch(machineLatchManager.getSectorLatch(), 45, "Timeout waiting for sector positioning")) {
                    return false;
                }

                machineLatchManager.createOpenLatch();

                if (awaitLatch(machineLatchManager.getOpenLatch(), 45, "Timeout waiting for door opening")) {
                    return false;
                }
            }

        }
        HttpStatus response = machineCommandService.openMachineDoor(device.getDrumId().toString(), device.getSectorId().toString());
        if (response != HttpStatus.OK) {
            socketResponse.sendOperationResponse("Failed", "Macchina non connessa, riavvio il dispenser", objectMapper);
            return false;
        }
        DeviceDTO deviceDTO = DeviceMapper.setDeviceDTO(device);
        socketResponse.sendTurnBackResponse("Success", "Carica il dispositivo", deviceDTO, objectMapper);
        Thread.sleep(machineProperties.getTimeCloseDoor());
        if (businessProperties.getMachineId().contains("900")) {
            machineLatchManager.createCloseLatch();

            if (awaitLatch(machineLatchManager.getCloseLatch(), 45, "Timeout waiting for door closing")) {
                return false;
            }
        }

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

    @Transactional
    public void deletePlanningById(String planningId) {
        List<Planning> plannings = planningRepository.findByPlanningId(planningId);
        planningRepository.deleteAll(plannings);
    }

    public boolean awaitLatch(CountDownLatch latch, int timeoutSeconds, String operationName) throws
            InterruptedException {
        boolean result = latch.await(timeoutSeconds, TimeUnit.SECONDS);
        if (!result) {
            log.warn(operationName + " non completato entro il timeout di " + timeoutSeconds + " secondi.");
        }
        return result;
    }

    public Planning getFirstActivePlanByEmployeeId(String employeeId) {
        log.info("data: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        log.info("ora: " + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
        log.info("emploee" + employeeId);

        List<Planning> list = planningRepository.findFirstByEmployeeIdAndTodayAndStartPlanAfter(employeeId,LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")), LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
        return list.isEmpty() ? null : list.getFirst();
    }

    public static String generateGuid() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replace("-", "");
    }


}
