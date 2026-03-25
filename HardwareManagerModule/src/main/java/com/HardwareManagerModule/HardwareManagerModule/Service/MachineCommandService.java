package com.HardwareManagerModule.HardwareManagerModule.Service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface MachineCommandService {
    HttpStatus positionMachineDoor(String drumId, String sectorId);

    HttpStatus sectorPositioningDoneRequest();

    HttpStatus startListening();

    HttpStatus stopListening();

    HttpStatus openMachineDoor(String drumId, String sectorId);

    HttpStatus openDoneRequest();

    HttpStatus closeMachineDoor(String drumId, String sectorId);

    HttpStatus closeDoneRequest();

    void restartSystem() throws IOException;

    HttpStatus resetMachineRequest();
}
