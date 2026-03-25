package com.ZCarFleetModule.ZCarFleetModule.Repository;


import com.ZCarFleetModule.ZCarFleetModule.Entity.EventToSend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SoapRepository extends JpaRepository<EventToSend, Long> {


}
