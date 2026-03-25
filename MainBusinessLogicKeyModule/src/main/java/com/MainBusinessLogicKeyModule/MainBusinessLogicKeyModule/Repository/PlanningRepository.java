package com.MainBusinessLogicKeyModule.MainBusinessLogicKeyModule.Repository;


import com.MainBusinessLogicKeyModule.MainBusinessLogicKeyModule.Entity.Planning;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlanningRepository extends JpaRepository<Planning, String> {
    @Query("SELECT p FROM Planning p " +
            "WHERE p.EmployeeId = :employeeId " +
            "AND FUNCTION('STR_TO_DATE', p.PlanningDate, '%d.%m.%Y') = FUNCTION('STR_TO_DATE', :currentDate, '%d.%m.%Y') " +
            "AND FUNCTION('STR_TO_DATE', p.StartPlan, '%H:%i') <= FUNCTION('STR_TO_DATE', :currentTime, '%H:%i') " +
            "ORDER BY p.PlanningDate ASC, p.StartPlan ASC")
    List<Planning> findFirstByEmployeeIdAndTodayAndStartPlanAfter(
            @Param("employeeId") String employeeId,
            @Param("currentDate") String currentDate,
            @Param("currentTime") String currentTime);

    @Modifying
    @Query("DELETE FROM Planning p WHERE p IN :plannings")
    void deleteAllByPlannings(@Param("plannings") List<Planning> plannings);

    @Query("SELECT p FROM Planning p WHERE p.PlanningId = :planningId")
    List<Planning> findByPlanningId(@Param("planningId") String planningId);

}
