package com.MainBusinessLogicDpiModule.MainBusinessLogicDpiModule.Service;

import com.MainBusinessLogicDpiModule.MainBusinessLogicDpiModule.Entity.EmployeeChoice;
import com.MainBusinessLogicDpiModule.MainBusinessLogicDpiModule.Repository.EmployeeChoiceRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class EmployeeChoiceServiceImpl implements EmployeeChoiceService {
    @Autowired
    EmployeeChoiceRepository employeeChoiceRepository;

    @Override
    public List<EmployeeChoice> getEmployeeChoice() {
        return employeeChoiceRepository.findAll();
    }

    @Override
    @Transactional
    public void addEmployeeChoice(List<Map<String, Object>> payloadList) {
        for (Map<String, Object> payload : payloadList) {
            EmployeeChoice employeeChoice = employeeChoiceRepository.findByDeviceTypeIdAndEmployeeId((String) payload.get("EmployeeId"),(String) payload.get("DeviceTypeId"));

            if (employeeChoice == null)
            {
                employeeChoice = new EmployeeChoice();
                employeeChoice.setCreditNumber((Integer) payload.get("CreditNumber"));
                employeeChoice.setCreditSpent((Integer) payload.get("CreditSpent"));
                employeeChoice.setDeviceTypeId((String) payload.get("DeviceTypeId"));
                employeeChoice.setEmployeeId((String) payload.get("EmployeeId"));
                employeeChoice.setResetCredit(payload.get("ResetCredit").equals("true"));
                employeeChoice.setRetiredDate((String) payload.get("RetiredDate"));

                employeeChoiceRepository.save(employeeChoice);
                log.info("Dispositivo con ID {} inserito con successo", employeeChoice.getEmployeeChoiceId());
            } else {
                log.info("employeechoice già esistente nel db");
            }
        }
    }

    @Override
    @Transactional
    public void updateEmployeeChoice(List<Map<String, Object>> payloadList) {
        for (Map<String, Object> payload : payloadList) {

            String deviceTypeId = payload.get("DeviceTypeId").toString();
            String employeeId = payload.get("EmployeeId").toString();
            log.info("Update Choice received on: " + employeeId + " " + deviceTypeId  + " " + payload);

            EmployeeChoice existingEmployeeChoice = employeeChoiceRepository.findByDeviceTypeIdAndEmployeeId(employeeId, deviceTypeId);

            if (existingEmployeeChoice != null) {
                existingEmployeeChoice.setCreditNumber((Integer) payload.get("CreditNumber"));
                existingEmployeeChoice.setCreditSpent((Integer) payload.get("CreditSpent"));
                existingEmployeeChoice.setDeviceTypeId((String) payload.get("DeviceTypeId"));
                existingEmployeeChoice.setEmployeeId((String) payload.get("EmployeeId"));
                existingEmployeeChoice.setResetCredit(payload.get("ResetCredit").equals("true"));
                existingEmployeeChoice.setRetiredDate((String) payload.get("RetiredDate"));

                employeeChoiceRepository.save(existingEmployeeChoice);
                log.info("Device updated successfully");
            } else {
                log.info("Device not found");
            }
        }
    }

    @Override
    @Transactional
    public void deleteEmployeeChoice(List<Map<String, Object>> payloadList) {
        for (Map<String, Object> payload : payloadList) {
            String deviceTypeId = payload.get("DeviceTypeId").toString();
            String employeeId = payload.get("EmployeeId").toString();

            EmployeeChoice existingEmployeeChoice = employeeChoiceRepository.findByDeviceTypeIdAndEmployeeId(employeeId, deviceTypeId);

            if (existingEmployeeChoice != null) {
                employeeChoiceRepository.delete(existingEmployeeChoice);
                log.info("Device deleted successfully");
            } else {
                log.info("Device not found");
            }
        }
    }

    @Override
    @Transactional
    public void deleteAllEmployeeChoice() {
        employeeChoiceRepository.deleteAll();
    }
}
