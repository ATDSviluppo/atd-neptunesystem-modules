package com.AuthenticationModule.Service;

import com.AuthenticationModule.Configuration.LatchManager;
import com.AuthenticationModule.Properties.AuthenticationProperties;
import com.AuthenticationModule.Repository.DeviceRepository;
import com.AuthenticationModule.Repository.EmployeeRepository;
import com.AuthenticationModule.Utility.OperationMode;
import com.CommonModule.CommonModule.DTO.UserChoiceDTO;
import com.CommonModule.CommonModule.Entity.Device;
import com.CommonModule.CommonModule.Entity.Employee;
import com.CommonModule.CommonModule.Service.CommonService;
import com.HMIModule.Response.SocketResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    @Autowired
    private SocketResponse socketResponse;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private AuthenticationProperties authenticationProperties;

    @Autowired
    private OperationMode operationMode;

    @Autowired
    private LatchManager latchManager;

    @Autowired
    private CommonService deliveryService;

    private String badgeCode;

    private static int cutFromFirst = 0;

    private static int cutFromLast = 0;

    private static String paddingFromFirst = "";

    private static String paddingFromLast = "";

    @PostConstruct
    public void init() {
        cutFromFirst = authenticationProperties.getCutFromFirst();
        cutFromLast = authenticationProperties.getCutFromLast();
        paddingFromFirst = authenticationProperties.getPaddingFromFirst();
        paddingFromLast = authenticationProperties.getPaddingFromLast();
    }

    @Override
    public synchronized String getBadgeCode() {
        return badgeCode;
    }

    @Override
    public synchronized void setBadgeCode(String badgeCode) {
        this.badgeCode = badgeCode;
    }

    @Override
    public ResponseEntity<String> analizeUserAuthentication(String employeeCard) throws IOException, InterruptedException {
        ObjectMapper objectMapper = new ObjectMapper();
        if (employeeCard != null && !employeeCard.isEmpty()) {

            String employeeCardPadded = employeeCard;

            if (employeeCard.length() > cutFromFirst + cutFromLast) {
                 employeeCardPadded = employeeCard.substring(cutFromFirst, employeeCard.length() - cutFromLast);
            }

            employeeCardPadded = paddingFromFirst + employeeCardPadded + paddingFromLast;
            log.info("code " + employeeCardPadded);
            Employee employee = employeeRepository.findByEmployeeCard(employeeCardPadded);
            setBadgeCode(employeeCardPadded);
            if (employee != null) {
                if (operationMode.isTurnbackMode()) {
                    latchManager.countDownBadgeScanLatch();
                } else if ("Charger".equals(employee.getEmployeeRole())) {
                    operationMode.setMode(OperationMode.CHARGE);
                } else if ("User".equals(employee.getEmployeeRole()) || "Assistant".equals(employee.getEmployeeRole())) {
                    operationMode.setMode(OperationMode.RETREAT);
                }
                handleEmployeeAuthentication(employee, employeeCard);
                return ResponseEntity.ok("Elaborazione completata per l'utente");
            }

            Device device = deviceRepository.findByEpcCode(employeeCardPadded);
            if (device != null) {
                return handleDeviceAuthentication(device, employeeCardPadded, objectMapper);
            }

            socketResponse.sendOperationResponse("Failed", "Utente non configurato " + employeeCardPadded, objectMapper);
            return ResponseEntity.status(404).body("Dispositivo o Utente non censito");

        } else {
            return null;
        }
    }

    @Transactional
    private ResponseEntity<String> handleEmployeeAuthentication(Employee employee, String employeeCard) throws JsonProcessingException {
        log.info("Badge letto: {} in modalità: {}", employeeCard, operationMode.getMode());
        ObjectMapper objectMapper = new ObjectMapper();
        if (operationMode.isRetreatMode() || operationMode.isChargeMode()) {
            setBadgeCode(employeeCard);
            socketResponse.sendOperationResponse("Welcome", "Benvenuto " + employee.getEmployeeName(), objectMapper);
            UserChoiceDTO device = deliveryService.analizeUserChoice(employee);
        } else if (operationMode.isTurnbackMode()) {
            latchManager.countDownBadgeScanLatch();
            operationMode.setMode(OperationMode.RETREAT);
        }

        return ResponseEntity.ok("Utente " + employee.getEmployeeName() + " trovato");
    }

    private ResponseEntity<String> handleDeviceAuthentication(Device device, String employeeCard, ObjectMapper objectMapper) throws IOException, InterruptedException {
        if (!Objects.equals(getBadgeCode(), "") && getBadgeCode() != null) {
            log.info("badge code" + getBadgeCode());
            Employee employee = employeeRepository.findByEmployeeCard(getBadgeCode());
            if (operationMode.isChargeMode()) {
                if (deliveryService.analizeUserCharge(employeeCard)) {
                    log.info("Dispositivo caricato");
                    OperationMode.setMode(OperationMode.RETREAT);
                    return ResponseEntity.ok("Dispositivo caricato");
                }
                log.info("Errore durante il caricamento");
                return ResponseEntity.ok("Errore durante il caricamento");
            }
            else {
                if (deliveryService.analizeUserTurnBack(employeeCard)) {
                    log.info("Dispositivo riconsegnato");
                    return ResponseEntity.ok("Dispositivo riconsegnato");
                }
            }
        }
        return null;
    }


    @Override
    public boolean isUserAuthenticate() {
        return !getBadgeCode().isEmpty();
    }

}
