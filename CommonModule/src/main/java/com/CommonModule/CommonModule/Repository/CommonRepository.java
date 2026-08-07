package com.CommonModule.CommonModule.Repository;

import com.CommonModule.CommonModule.Entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CommonRepository extends JpaRepository<Device, String> {
    @Modifying
    @Transactional
    @Query("UPDATE Device d SET d.Holder = true, d.TemporaryOwner = null WHERE d.DeviceId = :deviceId")
    int updateDeviceOn(@Param("deviceId") String deviceId);

    @Modifying
    @Transactional
    @Query("UPDATE Device d SET d.Holder = false, d.TemporaryOwner = :employeeId WHERE d.DeviceId = :deviceId")
    int updateDeviceOut(@Param("deviceId") String deviceId, @Param("employeeId") String employeeId);

    @Query("SELECT d FROM Device d WHERE d.Status = 'OK' AND d.Holder = true")
    List<Device> findByStatusAndHolder();

    @Query("SELECT d FROM Device d WHERE d.Holder = :holder")
    List<Device> findByHolder(@Param("holder") boolean holder);

    //mettere holder come parametro se si vuole gestire carico da portale ns
    @Query(value = """
            SELECT DISTINCT
                ed.Description AS DetailDescription,
                et.Description AS TypeDescription,
                et.enumDeviceTypeId,
                ed.DeviceDetailId
            FROM device d
            JOIN enumdevicedetail ed
                ON d.DeviceDetailId = ed.DeviceDetailId
            JOIN enumdevicetype et
                ON d.DeviceTypeId = et.enumDeviceTypeId
            WHERE d.Holder = true
            """, nativeQuery = true)
    List<Object[]> findDetailsAndTypesWhereHolderTrue();
}
