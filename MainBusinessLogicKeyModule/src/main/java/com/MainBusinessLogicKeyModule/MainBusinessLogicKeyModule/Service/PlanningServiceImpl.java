package com.MainBusinessLogicKeyModule.MainBusinessLogicKeyModule.Service;


import com.AuthenticationModule.Repository.EmployeeRepository;
import com.AuthenticationModule.Repository.EnumDeviceTypeRepository;
import com.CommonModule.CommonModule.Entity.Employee;
import com.CommonModule.CommonModule.Entity.EnumDeviceType;
import com.MainBusinessLogicKeyModule.MainBusinessLogicKeyModule.Entity.Planning;
import com.MainBusinessLogicKeyModule.MainBusinessLogicKeyModule.Repository.PlanningRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class PlanningServiceImpl implements PlanningService {
    @Autowired
    private PlanningRepository planningRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EnumDeviceTypeRepository enumDeviceTypeRepository;

    @Override
    public List<Planning> getPlanning() {
        return planningRepository.findAll();
    }

    @Override
    @Transactional
    public void addPlanning(List<Map<String, Object>> payloadList) {
        for (Map<String, Object> payload : payloadList) {
            Planning planning = new Planning();

            log.info("Planning chiamata");
            planning.setPlanningId((String) payload.get("PlanningID"));
            planning.setPlanningDate((String) payload.get("PlanningDate"));
            planning.setStartPlan((String) payload.get("StartPlan"));
            planning.setStopPlan((String) payload.get("StopPlan"));
            planning.setDeviceId((String) payload.get("DeviceId"));

            Optional<EnumDeviceType> deviceType = enumDeviceTypeRepository.findById("AUTO");
            if (deviceType.isPresent()) {
                planning.setEnumDeviceType(deviceType.get().getDeviceTypeId());
            } else {
                log.warn("EnumDeviceType con ID {} non trovato, non verrà associato al planning");
                planning.setEnumDeviceType(null);
            }

            String employeeId = (String) payload.get("EmployeeId");
            Optional<Employee> employee = employeeRepository.findById(employeeId);
            if (employee.isPresent()) {
                planning.setEmployeeId(employee.get().getEmployeeId());
            } else {
                log.info("Employee not found");
            }

            planningRepository.save(planning);
            log.info("Planning con ID {} inserito con successo", planning.getPlanningId());
        }
    }


    @Override
    @Transactional
    public void updatePlanning(List<Map<String, Object>> payloadList) {
        for (Map<String, Object> payload : payloadList) {
            String planningId = (String) payload.get("PlanningID");
            Optional<Planning> existingPlanning = planningRepository.findById(planningId);

            if (existingPlanning.isPresent()) {
                Planning planning = existingPlanning.get();
                planning.setPlanningDate((String) payload.get("PlanningDate"));
                planning.setStartPlan((String) payload.get("StartPlan"));
                planning.setStopPlan((String) payload.get("StopPlan"));
                planning.setDeviceId((String) payload.get("DeviceId"));

                String employeeId = (String) payload.get("EmployeeId");
                Optional<Employee> employee = employeeRepository.findById(employeeId);
                if (employee.isPresent()) {
                    planning.setEmployeeId(employee.get().getEmployeeId());
                } else {
                    log.info("Employee not found");
                }

                planningRepository.save(planning);
                log.info("Planning updated successfully");
            } else {
                log.info("Planning not found");
            }
        }
    }

    @Override
    @Transactional
    public void deletePlanning(List<Map<String, Object>> payloadList) {
        for (Map<String, Object> payload : payloadList) {
            String planningId = (String) payload.get("PlanningID");
            List<Planning> existingPlanning = planningRepository.findByPlanningId(planningId);

            if (existingPlanning != null && !existingPlanning.isEmpty()) {
                planningRepository.deleteAllByPlannings(existingPlanning);
                log.info("Planning deleted successfully");
            } else {
                log.info("Planning not found");
            }
        }
    }

    @Override
    public Planning getFirstActivePlanByEmployeeId(String employeeId) {
        LocalDate localDate = LocalDate.now();
        log.info("data: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        log.info("ora: " + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
        log.info("emploee" + employeeId);
        return planningRepository.findFirstByEmployeeIdAndTodayAndStartPlanAfter(employeeId, LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")), LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))).getFirst();
    }
}
