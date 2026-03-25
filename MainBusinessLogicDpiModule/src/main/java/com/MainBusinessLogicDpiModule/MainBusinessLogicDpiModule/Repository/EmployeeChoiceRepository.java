package com.MainBusinessLogicDpiModule.MainBusinessLogicDpiModule.Repository;

import com.MainBusinessLogicDpiModule.MainBusinessLogicDpiModule.Entity.EmployeeChoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeChoiceRepository extends JpaRepository<EmployeeChoice, Long> {
    @Query("SELECT ec FROM EmployeeChoice ec WHERE ec.employeeId = :employeeId AND ec.creditNumber - ec.creditSpent > 0")
    List<EmployeeChoice> findAllByEmployeeId(@Param("employeeId") String employeeId);

    @Query("SELECT ec FROM EmployeeChoice ec WHERE ec.employeeId = :employeeId AND ec.deviceTypeId = :deviceTypeId")
    EmployeeChoice findByDeviceTypeIdAndEmployeeId(@Param("employeeId") String employeeId, @Param("deviceTypeId") String deviceTypeId);
}
