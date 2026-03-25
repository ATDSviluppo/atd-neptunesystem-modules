package com.Mobile.MobileModule.Service;

import com.Mobile.MobileModule.DTO.EventDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface EventService {
    ResponseEntity<List<EventDTO>> getEvents();
}
