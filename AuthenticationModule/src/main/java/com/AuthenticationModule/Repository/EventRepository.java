package com.AuthenticationModule.Repository;


import com.CommonModule.CommonModule.Entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    @Query("SELECT e FROM Event e ORDER BY e.eventTimestamp DESC")
    List<Event> getEventsOrderByEventTimestamp();

    @Query("SELECT e FROM Event e WHERE e.toSend = true ORDER BY e.eventTimestamp")
    List<Event> findEventsToSave();

    @Modifying
    @Transactional
    @Query("UPDATE Event e SET e.toSend = false WHERE e.eventId = :eventId")
    int  updateEventSent(@Param("eventId") Long eventId);
}
