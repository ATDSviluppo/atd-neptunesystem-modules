package com.HardwareManagerModule.HardwareManagerModule.Service;

import com.CommonModule.CommonModule.Properties.BusinessProperties;
import com.HardwareManagerModule.HardwareManagerModule.Properties.MachineProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class MachineCommandServiceImpl implements MachineCommandService {
    @Autowired
    private MachineProperties machineProperties;

    @Autowired
    private BusinessProperties businessProperties;

    private final RestTemplate restTemplate;

    private String ipWebService;

    public MachineCommandServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostConstruct
    public void init() {
        this.ipWebService = machineProperties.getIpWebService();
    }

    @Override
    public HttpStatus positionMachineDoor(String drumId, String sectorId) {
        try {
            String url = "http://" + ipWebService + "/PositionSectorRequest";

            Map<String, String> singleBoxData = new HashMap<>();

            singleBoxData.put("SectorId", sectorId);
            log.info(businessProperties.getMachineId());
            if (businessProperties.getMachineId().contains("S2")) {
                singleBoxData.put("BoardId", drumId);
            } else {
                singleBoxData.put("BoardId", "brd" + drumId);
            }

            Map<String, Object> request = new HashMap<>();
            request.put("SingleBoxData", singleBoxData);

            ObjectMapper objectMapper = new ObjectMapper();
            String jsonPayload = objectMapper.writeValueAsString(request);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> requestEntity = new HttpEntity<>(jsonPayload, headers);

            log.info("Request Payload: " + jsonPayload);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                return HttpStatus.OK;
            } else {
                log.error("Unexpected response status: " + response.getStatusCode());
                return HttpStatus.INTERNAL_SERVER_ERROR;
            }
        } catch (Exception e) {
            log.error("Exception during HTTP request", e);
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }

    }


    @Override
    public HttpStatus sectorPositioningDoneRequest() {
        log.info("sectorPositioningDoneRequest chiamata");
        return HttpStatus.OK;
    }

    @Override
    public HttpStatus startListening() {
        try {
            String url = "http://" + ipWebService + "/StartListening";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                return HttpStatus.OK;
            } else {
                log.error("Unexpected response status: " + response.getStatusCode());
                return HttpStatus.INTERNAL_SERVER_ERROR;
            }
        } catch (Exception e) {
            log.error("Exception during HTTP request", e);
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }

    @Override
    public HttpStatus stopListening() {
        try {
            String url = "http://" + ipWebService + "/StopListening";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                return HttpStatus.OK;
            } else {
                log.error("Unexpected response status: " + response.getStatusCode());
                return HttpStatus.INTERNAL_SERVER_ERROR;
            }
        } catch (Exception e) {
            log.error("Exception during HTTP request", e);
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }


    @Override
    public HttpStatus openMachineDoor(String drumId, String sectorId) {
        try {
            if (!businessProperties.getMachineId().contains("MDS")) {
                String url = "http://" + ipWebService + "/OpenDoorRequest";
                String jsonPayload = getMachinePayload(drumId, sectorId);

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);

                HttpEntity<String> requestEntity = new HttpEntity<>(jsonPayload, headers);

                ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

                if (response.getStatusCode() == HttpStatus.OK) {
                    return HttpStatus.OK;
                } else {
                    log.error("Unexpected response status: " + response.getStatusCode());
                    return HttpStatus.INTERNAL_SERVER_ERROR;
                }
            } else {
                return HttpStatus.OK;
            }

        } catch (Exception e) {
            log.error("Exception during HTTP request", e);
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }

    }

    @Scheduled(cron = "0 00 00 * * ?")
    public void scheduledReboot() throws IOException {
        resetMachineRequest();
    }


    @Override
    public HttpStatus openDoneRequest() {
        log.info("openDoneRequest chiamata");
        return HttpStatus.OK;
    }

    @Override
    public HttpStatus closeMachineDoor(String drumId, String sectorId) {
        try {
            if (!businessProperties.getMachineId().contains("MDS")) {
                String url = "http://" + ipWebService + "/CloseDoorRequest";
                String jsonPayload = getMachinePayload(drumId, sectorId);

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);

                HttpEntity<String> requestEntity = new HttpEntity<>(jsonPayload, headers);

                ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

                if (response.getStatusCode() == HttpStatus.OK) {
                    return HttpStatus.OK;
                } else {
                    log.error("Unexpected response status: " + response.getStatusCode());
                    return HttpStatus.INTERNAL_SERVER_ERROR;
                }
            } else {
                return HttpStatus.OK;
            }
        } catch (Exception e) {
            log.error("Exception during HTTP request", e);
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }

    private String getMachinePayload(String drumId, String sectorId) throws JsonProcessingException {
        Map<String, String> singleBoxData = new HashMap<>();
        if (!businessProperties.getMachineId().equals("S2")) {
            singleBoxData.put("BoardId", "brd" + drumId);
        } else {
            singleBoxData.put("BoardId", drumId);
        }
        singleBoxData.put("SectorId", sectorId);

        Map<String, Object> request = new HashMap<>();
        request.put("SingleBoxData", singleBoxData);

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(request);
    }

    @Override
    public HttpStatus closeDoneRequest() {
        return HttpStatus.OK;
    }

    @Override
    public void restartSystem() throws IOException {
        String os = machineProperties.getOs().toLowerCase();
        ProcessBuilder processBuilder;

        if (os.contains("win")) {
            // Windows
            processBuilder = new ProcessBuilder("shutdown", "-r", "-t", "0");
        } else if (os.contains("nix") || os.contains("nux") || os.contains("mac")) {
            // Linux, Unix, MacOS
            processBuilder = new ProcessBuilder("sudo", "systemctl", "reboot");
        } else {
            throw new UnsupportedOperationException("Sistema operativo non supportato: " + os);
        }

        processBuilder.inheritIO();
        processBuilder.start();

    }

    @Override
    public HttpStatus resetMachineRequest() {
        try {
            String url = "http://" + ipWebService + "/ResetRequest";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>("", headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                return HttpStatus.OK;
            } else {
                log.error("Unexpected response status: " + response.getStatusCode());
                return HttpStatus.INTERNAL_SERVER_ERROR;
            }
        } catch (Exception e) {
            log.error("Exception during HTTP request", e);
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }

}





