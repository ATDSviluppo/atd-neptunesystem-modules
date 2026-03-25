package com.AuthenticationModule.Repository;

import com.CommonModule.CommonModule.Entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceRepository extends JpaRepository<Device, String> {
    @Query("SELECT d FROM Device d WHERE d.EcpCode = :ecpCode")
    Device findByEpcCode(@Param("ecpCode") String ecpCode);

}
