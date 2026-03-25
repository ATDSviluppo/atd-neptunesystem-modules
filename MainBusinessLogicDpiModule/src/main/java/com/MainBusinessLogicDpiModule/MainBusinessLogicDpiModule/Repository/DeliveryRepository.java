package com.MainBusinessLogicDpiModule.MainBusinessLogicDpiModule.Repository;


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

    @Modifying
    @Transactional
    @Query("UPDATE Device d SET d.Holder = true, d.TemporaryOwner = null, d.DeviceBarCode = :deviceBarCode WHERE d.DeviceId = :deviceId")
    int updateDeviceCharged(@Param("deviceId") String deviceId, @Param("deviceBarCode") String deviceBarCode);


    @Query("SELECT d FROM Device d WHERE d.DeviceDetail = :enumDeviceDetailId AND d.DeviceType = :enumDeviceTypeId AND d.Holder = :holder")
    List<Device> findByDeviceTypeAndDeviceDetailAndHolder(@Param("enumDeviceTypeId") String enumDeviceTypeId, @Param("enumDeviceDetailId") String enumDeviceDetailId, @Param("holder") boolean holder);

    @Query("SELECT d FROM Device d WHERE d.DeviceType IN :enumDeviceTypes AND d.Status = 'OK' AND d.Holder = true")
    List<Device> findByEnumDeviceTypeInAndStatusAndHolder(@Param("enumDeviceTypes") List<String> enumDeviceTypes);

    @Query("SELECT d FROM Device d WHERE d.DeviceType = :enumDeviceTypeId AND d.Status = 'OK' AND d.Holder = :holder GROUP BY d.DeviceDetail")
    List<Device> findByEnumDeviceTypeIdAndStatusAndHolder(@Param("enumDeviceTypeId") String enumDeviceTypeId, @Param("holder") boolean holder);

    @Query("SELECT CASE WHEN COUNT(d) > 0 THEN true ELSE false END FROM Device d WHERE d.TemporaryOwner = :employeeId")
    boolean hasDeviceToTurnBack(@Param("employeeId") String employeeId);

    @Query("""
                SELECT d.DeviceType, d.DeviceDetail, COUNT(d)
                FROM Device d
                WHERE d.Holder = :holder
                GROUP BY d.DeviceType, d.DeviceDetail
            """)
    List<Object[]> countByTypeAndDetail(@Param("holder") boolean holder);


    @Query("""
                SELECT COUNT(d)
                FROM Device d
                WHERE d.Holder = :holder AND d.DeviceType = :enumDeviceTypeId AND d.DeviceDetail = :enumDeviceDetailId
            """)
    int countByTypeAndDetailAndHolder(@Param("enumDeviceTypeId") String enumDeviceTypeId, @Param("enumDeviceDetailId") String enumDeviceDetailId, @Param("holder") boolean holder);


}
