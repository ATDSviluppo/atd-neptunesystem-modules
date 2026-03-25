package com.HardwareManagerModule.HardwareManagerModule.Repository;

import com.HardwareManagerModule.HardwareManagerModule.Entity.MachineConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MachineConfigRepository extends JpaRepository<MachineConfig,String> {
}
