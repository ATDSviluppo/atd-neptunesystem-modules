package com.MainBusinessLogicKeyModule.MainBusinessLogicKeyModule.Repository;


import com.CommonModule.CommonModule.Entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface DeliveryRepository extends JpaRepository<Device, String> {

    @Query("SELECT CASE WHEN COUNT(d) > 0 THEN true ELSE false END FROM Device d WHERE d.TemporaryOwner = :employeeId")
    boolean hasDeviceToTurnBack(@Param("employeeId") String employeeId);


}
