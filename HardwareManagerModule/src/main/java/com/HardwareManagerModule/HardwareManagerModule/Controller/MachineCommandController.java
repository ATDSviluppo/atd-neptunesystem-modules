package com.HardwareManagerModule.HardwareManagerModule.Controller;

import com.HardwareManagerModule.HardwareManagerModule.Latch.MachineLatchManager;
import com.HardwareManagerModule.HardwareManagerModule.Service.MachineCommandService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@Slf4j
public class MachineCommandController {
    @Autowired
    private MachineCommandService machineCommandService;

    @Autowired
    private MachineLatchManager machineLatchManager;

    @CrossOrigin(origins = "*")
    @PostMapping("/api/sectorpositioningdonerequest")
    public void positionDoneMachineDoor() {
        log.info("SectorPositioningDoneRequest chiamata");
        machineCommandService.sectorPositioningDoneRequest();
        machineLatchManager.countDownSectorLatch();
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/api/opendonerequest")
    public void openDoneRequest() {
        log.info("OpenDoneRequest chiamata");
        machineCommandService.openDoneRequest();
        machineLatchManager.countDownOpenLatch();
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/api/closedonerequest")
    public void closeDoneRequest() throws InterruptedException {
        log.info("CloseDoneRequest chiamata");
        machineCommandService.closeDoneRequest();
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/PositionMachineDoor")
    public void positionMachineDoor(@RequestBody Map<String, String> payload) {
        String drumId = payload.get("drumId");
        String sectorId = payload.get("sectorId");
        machineCommandService.positionMachineDoor(drumId, sectorId);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/api/OpenMachineDoor")
    public void openMachineDoor(@RequestBody Map<String, String> payload) {
        String drumId = payload.get("drumId");
        String sectorId = payload.get("sectorId");
        machineCommandService.openMachineDoor(drumId, sectorId);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/api/CloseMachineDoor")
    public HttpStatus closeMachineDoor(@RequestBody Map<String, String> payload) throws InterruptedException {
        log.info("closeMachineDoor chiamata");
        String drumId = payload.get("drumId");
        String sectorId = payload.get("sectorId");
        machineCommandService.closeMachineDoor(drumId, sectorId);
        return HttpStatus.OK;
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/api/StartListening")
    public HttpStatus startListening() {
        return machineCommandService.startListening();
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/api/StopListening")
    public HttpStatus stopListening() {
       return machineCommandService.stopListening();
    }

    @GetMapping("/RestartSystem")
    public void restartSystem() throws IOException { machineCommandService.restartSystem(); }
}
