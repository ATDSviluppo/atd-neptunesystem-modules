package com.NeptuneWebAutheticator.Repository;

import com.NeptuneWebAutheticator.Entity.Machine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MachineRepository extends JpaRepository<Machine, String> {
    @Query("SELECT m FROM Machine m WHERE m.tenant.tenantId = :tenantId")
    List<Machine> findByTenantId(@Param("tenantId") Long tenantId);

    @Query("SELECT m FROM Machine m WHERE m.machineId = :machineId AND m.tenant.tenantId = :tenantId")
    List<Machine> findByMachineIdAndTenantId(@Param("machineId") String machineId, @Param("tenantId") Long tenantId);
}
