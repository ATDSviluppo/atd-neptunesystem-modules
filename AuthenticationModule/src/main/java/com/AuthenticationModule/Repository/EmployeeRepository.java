package com.AuthenticationModule.Repository;


import com.CommonModule.CommonModule.Entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String> {
    @Query("SELECT e FROM Employee e WHERE e.employeeCard = :employeeCard")
    Employee findByEmployeeCard(@Param("employeeCard") String employeeCard);
}
