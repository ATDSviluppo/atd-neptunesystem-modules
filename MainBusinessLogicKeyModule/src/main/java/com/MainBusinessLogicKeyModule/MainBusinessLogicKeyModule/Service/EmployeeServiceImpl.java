package com.MainBusinessLogicKeyModule.MainBusinessLogicKeyModule.Service;

import com.AuthenticationModule.Repository.EmployeeRepository;
import com.CommonModule.CommonModule.Entity.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public List<Employee> getEmployee() {
        return employeeRepository.findAll();
    }

    @Override
    @Transactional
    public ResponseEntity<String> addEmployee(Object payload) {
        // Verifica se il payload è un array o un singolo oggetto
        switch (payload) {
            case List list when ((List<?>) payload).size() > 1 -> {
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> payloadList = (List<Map<String, Object>>) list;
                for (Map<String, Object> singlePayload : payloadList) {
                    Employee employee = new Employee();

                    log.info("Aggiornamento impiegato: ID = {}, Role = {}, Name = {}, Card = {}, Pin = {}",
                            singlePayload.get("EmployeeId"),
                            singlePayload.get("EmployeeRole"),
                            singlePayload.get("EmployeeName"),
                            singlePayload.get("EmployeeCard"),
                            singlePayload.get("EmployeePin"));

                    employee.setEmployeeId((String) singlePayload.get("EmployeeId"));
                    employee.setEmployeeRole((String) singlePayload.get("EmployeeRole"));
                    employee.setEmployeeName((String) singlePayload.get("EmployeeName"));
                    employee.setEmployeeCard((String) singlePayload.get("EmployeeCard"));

                    Optional<Employee> employeeExisting = employeeRepository.findById((String) singlePayload.get("EmployeeId"));
                    Employee emp = employeeRepository.findByEmployeeCard(employee.getEmployeeCard());

                    if (employeeExisting.isPresent()) {
                        log.info("Impiegato con ID {} già esistente, non verrà inserito", singlePayload.get("EmployeeId"));
                        return ResponseEntity.ok("Impiegato già esistente, non verrà inserito");
                    } else if (emp != null) {
                        return ResponseEntity.status(409).body("Codice badge già registrato, non verrà inserito");
                    } else {
                        employeeRepository.save(employee);
                        log.info("Impiegato con ID {} inserito con successo", employee.getEmployeeId());
                        return ResponseEntity.ok("Inserimento avvenuto con successo");
                    }
                }
            }
            case List list when ((List<?>) payload).size() == 1 -> {
                @SuppressWarnings("unchecked")
                Map<String, Object> singlePayload = (Map<String, Object>) list.getFirst();
                Employee employee = new Employee();

                log.info("Aggiornamento impiegato: ID = {}, Role = {}, Name = {}, Card = {}, Pin = {}",
                        singlePayload.get("EmployeeId"),
                        singlePayload.get("EmployeeRole"),
                        singlePayload.get("EmployeeName"),
                        singlePayload.get("EmployeeCard"),
                        singlePayload.get("EmployeePin"));

                employee.setEmployeeId((String) singlePayload.get("EmployeeId"));
                employee.setEmployeeRole((String) singlePayload.get("EmployeeRole"));
                employee.setEmployeeName((String) singlePayload.get("EmployeeName"));
                employee.setEmployeeCard((String) singlePayload.get("EmployeeCard"));

                Optional<Employee> employeeExisting = employeeRepository.findById((String) singlePayload.get("EmployeeId"));
                Employee emp = employeeRepository.findByEmployeeCard(employee.getEmployeeCard());
                if (employeeExisting.isPresent()) {
                    log.info("Impiegato con ID {} già esistente, non verrà inserito", singlePayload.get("EmployeeId"));
                    return ResponseEntity.status(200).body("Impiegato già esistente, non verrà inserito");
                } else if (emp != null) {
                    return ResponseEntity.status(409).body("Codice badge già registrato, non verrà inserito");
                } else {
                    employeeRepository.save(employee);
                    log.info("Impiegato con ID {} inserito con successo", employee.getEmployeeId());
                    return ResponseEntity.ok("Inserimento avvenuto con successo");
                }
            }
            case Map map -> {
                @SuppressWarnings("unchecked")
                Map<String, Object> singlePayload = (Map<String, Object>) payload;
                Employee employee = new Employee();

                log.info("Aggiornamento impiegato: ID = {}, Role = {}, Name = {}, Card = {}, Pin = {}",
                        singlePayload.get("EmployeeId"),
                        singlePayload.get("EmployeeRole"),
                        singlePayload.get("EmployeeName"),
                        singlePayload.get("EmployeeCard"),
                        singlePayload.get("EmployeePin"));

                employee.setEmployeeId((String) singlePayload.get("EmployeeId"));
                employee.setEmployeeRole((String) singlePayload.get("EmployeeRole"));
                employee.setEmployeeName((String) singlePayload.get("EmployeeName"));
                employee.setEmployeeCard((String) singlePayload.get("EmployeeCard"));

                Optional<Employee> employeeExisting = employeeRepository.findById((String) singlePayload.get("EmployeeId"));

                Employee emp = employeeRepository.findByEmployeeCard(employee.getEmployeeCard());

                if (employeeExisting.isPresent()) {
                    log.info("Impiegato con ID {} già esistente, non verrà inserito", singlePayload.get("EmployeeId"));
                    return ResponseEntity.status(200).body("Impiegato già esistente, non verrà inserito");
                } else if (emp != null) {
                    return ResponseEntity.status(409).body("Codice badge già registrato, non verrà inserito");
                } else {
                    employeeRepository.save(employee);
                    log.info("Impiegato con ID {} inserito con successo", employee.getEmployeeId());
                    return ResponseEntity.ok("Inserimento avvenuto con successo");
                }
            }
            case null, default -> {
                return ResponseEntity.status(400).body("Payload malformed");
            }
        }
        return null;
    }

    @Override
    @Transactional
    public ResponseEntity<String> updateEmployee(Map<String, Object> payload) {
        String employeeId = (String) payload.get("EmployeeId");
        Optional<Employee> existingEmployee = employeeRepository.findById(employeeId);

        if (existingEmployee.isPresent()) {
            Employee employee = existingEmployee.get();
            employee.setEmployeeName((String) payload.get("EmployeeName"));
            employee.setEmployeeRole((String) payload.get("EmployeeRole"));
            employee.setEmployeeCard((String) payload.get("EmployeeCard"));

            employeeRepository.save(employee);
            log.info("Employee updated successfully");
            return ResponseEntity.ok("Aggiornamento avvenuto con successo");
        } else {
            log.info("Employee not found");
            return ResponseEntity.status(404).body("Utente non trovato");
        }
    }

    @Override
    @Transactional
    public ResponseEntity<String> deleteEmployee(Map<String, Object> payload) {
        String employeeId = (String) payload.get("EmployeeId");
        Optional<Employee> existingEmployee = employeeRepository.findById(employeeId);

        if (existingEmployee.isPresent()) {
            employeeRepository.delete(existingEmployee.get());
            log.info("Employee deleted successfully");
            return ResponseEntity.ok("Eliminazione avvenuta con successo");
        } else {
            log.info("Employee not found");
            return ResponseEntity.status(404).body("Utente non trovato");
        }
    }
}
