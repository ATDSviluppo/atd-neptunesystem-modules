package com.MainBusinessLogicDpiModule.MainBusinessLogicDpiModule.Service;

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
        if (payload instanceof List) {
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> payloadList = (List<Map<String, Object>>) payload;
            int numeroUtenti = payloadList.size();
            int utentiAggiunti = 0;
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
                if (employeeExisting.isPresent()) {
                    log.info("Impiegato con ID {} già esistente, non verrà inserito", singlePayload.get("EmployeeId"));
                } else {
                    employeeRepository.save(employee);
                    utentiAggiunti++;
                    log.info("Impiegato con ID {} inserito con successo", employee.getEmployeeId());
                }
                return ResponseEntity.ok("Importazione di " + utentiAggiunti + " su " + numeroUtenti);
            }
        } else if (payload instanceof Map) {
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
            if (employeeExisting.isPresent()) {
                log.info("Impiegato  già esistente, non verrà inserito");
                return ResponseEntity.status(500).body("Utente già contenuto, non verrà inserito");

            } else {
                employeeRepository.save(employee);
                log.info("Impiegato con ID {} inserito con successo", employee.getEmployeeId());
                return ResponseEntity.ok("Utente inserito con successo");

            }
        }
        return ResponseEntity.status(500).body("Errore nell'invio dati");

    }

    @Override
    @Transactional
    public ResponseEntity<String> updateEmployee(Object payload) {
        if (payload instanceof List) {
            List<Map<String, Object>> payloadList = (List<Map<String, Object>>) payload;
            int numeroUtenti = payloadList.size();
            int utentiAggiunti = 0;
            for (Map<String, Object> singlePayload : payloadList) {
                String employeeId = (String) singlePayload.get("EmployeeId");
                Optional<Employee> existingEmployee = employeeRepository.findById(employeeId);

                if (existingEmployee.isPresent()) {
                    Employee employee = existingEmployee.get();
                    employee.setEmployeeName((String) singlePayload.get("EmployeeName"));
                    employee.setEmployeeRole((String) singlePayload.get("EmployeeRole"));
                    employee.setEmployeeCard((String) singlePayload.get("EmployeeCard"));

                    employeeRepository.save(employee);
                    log.info("Employee updated successfully");
                    utentiAggiunti++;
                } else {
                    log.info("Employee not found");
                }
                return ResponseEntity.ok("Aggiornamento di " + utentiAggiunti + " su " + numeroUtenti);
            }

        } else if (payload instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> singlePayload = (Map<String, Object>) payload;
            String employeeId = (String) singlePayload.get("EmployeeId");
            Optional<Employee> existingEmployee = employeeRepository.findById(employeeId);

            if (existingEmployee.isPresent()) {
                Employee employee = existingEmployee.get();
                employee.setEmployeeName((String) singlePayload.get("EmployeeName"));
                employee.setEmployeeRole((String) singlePayload.get("EmployeeRole"));
                employee.setEmployeeCard((String) singlePayload.get("EmployeeCard"));

                employeeRepository.save(employee);
                log.info("Employee updated successfully");
                return ResponseEntity.ok("Utente aggiornato con successo");


            } else {
                log.info("Employee not found");
                return ResponseEntity.status(404).body("Utente non trovato");
            }
        }
        return ResponseEntity.status(500).body("Errore nell'invio dati");
    }

    @Override
    @Transactional
    public ResponseEntity<String> deleteEmployee(Object payload) {
        if (payload instanceof List) {
            List<Map<String, Object>> payloadList = (List<Map<String, Object>>) payload;
            int numeroUtenti = payloadList.size();
            int utentiAggiunti = 0;

            for (Map<String, Object> singlePayload : payloadList) {
                String employeeId = (String) singlePayload.get("EmployeeId");
                Optional<Employee> existingEmployee = employeeRepository.findById(employeeId);

                if (existingEmployee.isPresent()) {
                    employeeRepository.delete(existingEmployee.get());
                    log.info("Employee deleted successfully");
                    utentiAggiunti++;
                } else {
                    log.info("Employee not found");
                }
            }
            return ResponseEntity.ok("Eliminazione di " + utentiAggiunti + " su " + numeroUtenti);

        } else if (payload instanceof Map) {
            Map<String, Object> singlePayload = (Map<String, Object>) payload;

            String employeeId = (String) singlePayload.get("EmployeeId");
            Optional<Employee> existingEmployee = employeeRepository.findById(employeeId);

            if (existingEmployee.isPresent()) {
                employeeRepository.delete(existingEmployee.get());
                log.info("Employee deleted successfully");
                return ResponseEntity.ok("Utente eliminato con successo");
            } else {
                log.info("Employee not found");
                return ResponseEntity.status(404).body("Utente non trovato");
            }
        }
        return ResponseEntity.status(500).body("Errore nell'invio dati");
    }
}
