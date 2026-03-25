package com.Safety.SafetyModule.Repository;


import com.Safety.SafetyModule.Entity.EventToSend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SoapRepository extends JpaRepository<EventToSend, Long> {


}
