package com.NeptuneWebAutheticator.Service;

import com.NeptuneWebAutheticator.DTO.MachineDTO;
import com.NeptuneWebAutheticator.Entity.Machine;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public interface MachineService {
    List<Machine> getMachines();

    List<MachineDTO> getMachinesByTenantId(HttpServletRequest request);

    @Transactional
    ResponseEntity<String> addMachine(HttpServletRequest request, Map<String, Object> payload);

    @Transactional
    ResponseEntity<String> updateMachine(Map<String, Object> payloadList);

    @Transactional
    ResponseEntity<String> deleteMachine(HttpServletRequest request, Map<String, Object> payloadList);

    @Transactional
    ResponseEntity<String> addTenant(HttpServletRequest request, Map<String, Object> payloadList);

    @Transactional
    ResponseEntity<String> updateTenant(HttpServletRequest request, Map<String, Object> payloadList);
}
