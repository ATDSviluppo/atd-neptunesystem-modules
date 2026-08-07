package com.MainBusinessLogicKeyModule.MainBusinessLogicKeyModule.Repository;

import com.MainBusinessLogicKeyModule.MainBusinessLogicKeyModule.Entity.Planning;
import com.MainBusinessLogicKeyModule.MainBusinessLogicKeyModule.Entity.PlanningRetriedMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlanningRetriedMapRepository extends JpaRepository<PlanningRetriedMap, Long> {
    @Query("SELECT p FROM PlanningRetriedMap p WHERE LOWER(p.DeviceId) = LOWER(:deviceId)")
    List<PlanningRetriedMap> findByDeviceId(@Param("deviceId") String deviceId);

    @Modifying
    @Query("DELETE FROM PlanningRetriedMap p WHERE p.PlanningId = :planningId")
    void deleteByPlanningId(@Param("planningId") String planningId);

    @Query("Select p FROM PlanningRetriedMap p WHERE p.DeviceId = :deviceId AND p.EmployeeId = :employeeId")
    PlanningRetriedMap findByDeviceIdAndEmployeeId(@Param("deviceId") String deviceId,@Param("employeeId") String employeeId);

    @Query("Select p FROM PlanningRetriedMap p WHERE p.PlanningId = :planningId")
    PlanningRetriedMap findByPlanningId(@Param("planningId") String planningId);
}
