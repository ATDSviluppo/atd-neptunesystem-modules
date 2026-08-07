package com.NeptuneWebAutheticator.Service;

import com.NeptuneWebAutheticator.DTO.MachineDTO;
import com.NeptuneWebAutheticator.Entity.Machine;
import com.NeptuneWebAutheticator.Entity.Tenant;
import com.NeptuneWebAutheticator.Repository.MachineRepository;
import com.NeptuneWebAutheticator.Repository.TenantRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class MachineServiceImpl implements MachineService {
    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private MachineRepository machineRepository;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private TenantRepository tenantRepository;

    @Override
    public List<Machine> getMachines() {
        return null;
    }

    @Override
    @Transactional
    public List<MachineDTO> getMachinesByTenantId(HttpServletRequest request) {
        if (authenticationService.isUserAuthenticate(request)) {
            Tenant tenant = authenticationService.getUserAuthenticated(request);
            List<MachineDTO> machineDTOList = new ArrayList<>();
            List<Machine> machineList = machineRepository.findByTenantId(tenant.getTenantId());
            for (Machine machine : machineList) {
                MachineDTO machineDTO = new MachineDTO();
                machineDTO.setMachineid(machine.getMachineId());
                machineDTO.setIp_address(machine.getIpAddress());
                machineDTO.setTruckingOn(machine.isTruckingOn());
                machineDTOList.add(machineDTO);
            }
            return machineDTOList;
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    @Override
    @Transactional
    public ResponseEntity<String> addMachine(HttpServletRequest request, Map<String, Object> payload) {
        if (authenticationService.isUserAuthenticate(request)) {
            Tenant tenant = authenticationService.getUserAuthenticated(request);
            if (tenant == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

            List<Machine> machineList = machineRepository.findByMachineIdAndTenantId((String) payload.get("MachineId"), tenant.getTenantId());
            if (machineList == null || machineList.isEmpty()) {
                Machine machine = new Machine();
                machine.setMachineId((String) payload.get("MachineId"));
                machine.setTenant(tenant);
                machine.setIpAddress((String) payload.get("IpValue"));
                Object v = payload.get("isTruckingOn");
                boolean truckingOn = v instanceof Boolean
                        ? (Boolean) v
                        : v instanceof Number && ((Number) v).intValue() == 1;

                machine.setTruckingOn(truckingOn);
                machineRepository.save(machine);
                return ResponseEntity.ok("Macchina aggiunta con successo");
            } else {
                return ResponseEntity.ok("Macchina già esistente");
            }

        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    @Override
    public ResponseEntity<String> updateMachine(Map<String, Object> payloadList) {
        return null;
    }

    @Override
    @Transactional
    public ResponseEntity<String> deleteMachine(HttpServletRequest request, Map<String, Object> payloadList) {
        if (authenticationService.isUserAuthenticate(request)) {
            Tenant tenant = authenticationService.getUserAuthenticated(request);
            if (tenant == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
            String machineId = (String) payloadList.get("MachineId");
            List<Machine> machineList = machineRepository.findByMachineIdAndTenantId(machineId, tenant.getTenantId());
            if (machineList == null || machineList.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            machineRepository.deleteAll(machineList);
            return ResponseEntity.ok("Macchina eliminata con successo");
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    @Override
    @Transactional
    public ResponseEntity<String> addTenant(HttpServletRequest request, Map<String, Object> payloadList) {
        if (authenticationService.isUserAuthenticate(request) && Objects.equals(authenticationService.getUserAuthenticated(request).getRole(), "Administrator")) {
            String role = (String) payloadList.get("Role");
            String username = (String) payloadList.get("Username");
            String password = passwordEncoder().encode((String) payloadList.get("Password"));

            if (role == null || username == null || password == null) {
                return ResponseEntity.status(400).body("Payload malformed");
            }

            if (tenantRepository.findByUsername(username) != null) {
                return ResponseEntity.status(400).body("Username already taken");
            }

            Tenant tenant = new Tenant();
            tenant.setRole(role);
            tenant.setUsername(username);
            tenant.setPassword(password);

            tenantRepository.save(tenant);
            return ResponseEntity.ok("Utente aggiunto con successo");
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    @Override
    @Transactional
    public ResponseEntity<String> updateTenant(HttpServletRequest request, Map<String, Object> payloadList) {
        if (authenticationService.isUserAuthenticate(request)) {
            String username = (String) payloadList.get("Username");
            String password = passwordEncoder().encode((String) payloadList.get("Password"));

            if (username == null || password == null) {
                return ResponseEntity.status(400).body("Payload malformed");
            }

            if (tenantRepository.findByUsername(username) != null) {
                return ResponseEntity.status(400).body("Username already taken");
            }

            Tenant tenant = authenticationService.getUserAuthenticated(request);
            tenant.setUsername(username);
            tenant.setPassword(password);
            tenantRepository.save(tenant);
            return ResponseEntity.ok("Utente aggiornato con successo");
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }
}
