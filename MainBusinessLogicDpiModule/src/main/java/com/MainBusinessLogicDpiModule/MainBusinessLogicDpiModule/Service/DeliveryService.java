package com.MainBusinessLogicDpiModule.MainBusinessLogicDpiModule.Service;

import com.CommonModule.CommonModule.Entity.Device;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;

@Service
public interface DeliveryService {
    void setDeviceToTurnBack(Device device);

    boolean analizeMassiveCharger() throws JsonProcessingException;

}
