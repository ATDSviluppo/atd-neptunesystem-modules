package com.Mobile.MobileModule.Service;

import com.AuthenticationModule.Repository.EmployeeRepository;
import com.AuthenticationModule.Repository.EnumDeviceDetailRepository;
import com.AuthenticationModule.Repository.EnumDeviceTypeRepository;
import com.AuthenticationModule.Repository.EventRepository;
import com.CommonModule.CommonModule.Entity.Employee;
import com.CommonModule.CommonModule.Entity.EnumDeviceDetail;
import com.CommonModule.CommonModule.Entity.EnumDeviceType;
import com.CommonModule.CommonModule.Entity.Event;
import com.CommonModule.CommonModule.Properties.BusinessProperties;
import com.Mobile.MobileModule.DTO.EventDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EventServiceImpl implements EventService {
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private EnumDeviceDetailRepository enumDeviceDetailRepository;
    @Autowired
    private EnumDeviceTypeRepository enumDeviceTypeRepository;
    @Autowired
    private BusinessProperties businessProperties;

    @Override
    public ResponseEntity<List<EventDTO>> getEvents() {
        List<Event> events = eventRepository.getEventsOrderByEventTimestamp();
        List<EventDTO> eventDTOList = new ArrayList<>();

        for (Event event : events) {
            String employeeName = Optional.ofNullable(event.getEmployeeId())
                    .flatMap(employeeRepository::findById)
                    .map(Employee::getEmployeeName)
                    .orElse("UNKNOWN");

            String enumDeviceTypeDesc = Optional.ofNullable(event.getEnumDeviceTypeId())
                    .flatMap(enumDeviceTypeRepository::findById)
                    .map(EnumDeviceType::getDescription)
                    .orElse("UNKNOWN");

            String enumDeviceDetailDesc = Optional.ofNullable(event.getDeviceDetailId())
                    .flatMap(enumDeviceDetailRepository::findById)
                    .map(EnumDeviceDetail::getDescription)
                    .orElse("UNKNOWN");


            EventDTO eventDTO = new EventDTO();
            eventDTO.setEventType(event.getEventType());
            eventDTO.setEventTimeStamp(event.getEventTimestamp());
            eventDTO.setEmployee(employeeName);
            eventDTO.setDeviceId(event.getDeviceId());
            eventDTO.setNote(event.getNote());
            if (businessProperties.isTruckingOn()) {
                eventDTO.setObjectId(enumDeviceTypeDesc + " taglia " + enumDeviceDetailDesc);
            } else {
                eventDTO.setObjectId(event.getDeviceId());
            }
            eventDTOList.add(eventDTO);
        }

        return ResponseEntity.ok(eventDTOList);
    }
}
