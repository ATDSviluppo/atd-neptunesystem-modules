package com.HMIModule.Response;

import com.CommonModule.CommonModule.Entity.Employee;
import com.CommonModule.CommonModule.Properties.BusinessProperties;
import com.HMIModule.DTO.ApiResponseDTO;
import com.HMIModule.DTO.OperationResponseDTO;
import com.HMIModule.DTO.SendDeviceDTO;
import com.HMIModule.DTO.WebSocketDTO;
import com.HMIModule.Handler.SocketHandler;
import com.HMIModule.Properties.HMIProperties;
import com.CommonModule.CommonModule.DTO.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
@Slf4j
public class SocketResponse {
    private static final SocketHandler socketHandler = new SocketHandler();

    @Autowired
    HMIProperties hmiProperties;

    @Autowired
    BusinessProperties businessProperties;

    private static String machineId = "";
    private static String keyword = "";

    @PostConstruct
    public void init() {
        machineId = businessProperties.getMachineId();
        keyword = hmiProperties.getKeyword();
    }

    private static final CopyOnWriteArrayList<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    public void sendOperationResponse(String status, String messageText, ObjectMapper objectMapper) throws JsonProcessingException {
        OperationResponseDTO responseDTO = new OperationResponseDTO(status, messageText);
        ApiResponseDTO apiResponseDTO = new ApiResponseDTO("TPNTE", objectMapper.writeValueAsString(responseDTO));
        String message = objectMapper.writeValueAsString(apiResponseDTO);
        log.info(message);
        socketHandler.sendMessageToAll(message);
    }

    public void sendChargeResponse(String status, String messageText, ObjectMapper objectMapper) throws JsonProcessingException {
        OperationResponseDTO responseDTO = new OperationResponseDTO(status, messageText);
        ApiResponseDTO apiResponseDTO = new ApiResponseDTO("TP008", objectMapper.writeValueAsString(responseDTO));
        String message = objectMapper.writeValueAsString(apiResponseDTO);
        log.info(message);
        socketHandler.sendMessageToAll(message);
    }

    public void sendDownloadResponse(String status, String messageText, ObjectMapper objectMapper) throws JsonProcessingException {
        OperationResponseDTO responseDTO = new OperationResponseDTO(status, messageText);
        ApiResponseDTO apiResponseDTO = new ApiResponseDTO("TP008", objectMapper.writeValueAsString(responseDTO));
        String message = objectMapper.writeValueAsString(apiResponseDTO);
        log.info(message);
        socketHandler.sendMessageToAll(message);
    }

    public void sendConnectionToSocket() throws JsonProcessingException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            log.info("machineId" + machineId);
            log.info("o end or no end");
            WebSocketDTO webSocketDTO = new WebSocketDTO(keyword, machineId);
            String webSocketData = objectMapper.writeValueAsString(webSocketDTO);
            ApiResponseDTO apiResponseDTO = new ApiResponseDTO("TPMSG", webSocketData);
            String message = objectMapper.writeValueAsString(apiResponseDTO);
            socketHandler.sendMessageToAll(String.valueOf(message));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public void sendRetreatResponse(String status, String messageText, DeviceDTO deviceDTO, ObjectMapper objectMapper) throws JsonProcessingException {
        OperationResponseDTO responseDTO = new OperationResponseDTO(status, messageText);
        ApiResponseDTO apiResponseDTO = new ApiResponseDTO("TP006", objectMapper.writeValueAsString(responseDTO));
        SendDeviceDTO sendDeviceDTO = new SendDeviceDTO(apiResponseDTO, deviceDTO);
        String message = objectMapper.writeValueAsString(sendDeviceDTO);
        log.info(message);
        socketHandler.sendMessageToAll(message);
    }

    public void sendTurnBackResponse(String status, String messageText, DeviceDTO deviceDTO, ObjectMapper objectMapper) throws JsonProcessingException {
        OperationResponseDTO responseDTO = new OperationResponseDTO(status, messageText);
        ApiResponseDTO apiResponseDTO = new ApiResponseDTO("TP005", objectMapper.writeValueAsString(responseDTO));
        SendDeviceDTO sendDeviceDTO = new SendDeviceDTO(apiResponseDTO, deviceDTO);
        String message = objectMapper.writeValueAsString(sendDeviceDTO);
        log.info(message);
        socketHandler.sendMessageToAll(message);
    }

    public void sendUserChoiceToSocket(UserChoiceDTO userChoiceDTO, ObjectMapper objectMapper) throws JsonProcessingException {
        String userChoiceJson = objectMapper.writeValueAsString(userChoiceDTO);
        ApiResponseDTO apiResponseDTO = new ApiResponseDTO("TP001", userChoiceJson);
        String message = objectMapper.writeValueAsString(apiResponseDTO);
        //log.info(message);
        socketHandler.sendMessageToAll(message);
    }

    public void sendEmployeeToSocket(List<Employee> employeeList, ObjectMapper objectMapper) throws JsonProcessingException {
        String employeeJson = objectMapper.writeValueAsString(employeeList);
        ApiResponseDTO apiResponseDTO = new ApiResponseDTO("TP002", employeeJson);
        String message = objectMapper.writeValueAsString(apiResponseDTO);
        socketHandler.sendMessageToAll(message);
    }

    public void sendTurnBackNote(String status, String messageText, ObjectMapper objectMapper) throws JsonProcessingException {
        OperationResponseDTO responseDTO = new OperationResponseDTO(status, messageText);
        ApiResponseDTO apiResponseDTO = new ApiResponseDTO("TP007", objectMapper.writeValueAsString(responseDTO));
        String message = objectMapper.writeValueAsString(apiResponseDTO);
        log.info(message);
        socketHandler.sendMessageToAll(message);
    }

    public void sendSystemNote(String status, String messageText, DeviceDTO deviceDTO,ObjectMapper objectMapper) throws JsonProcessingException {
        OperationResponseDTO responseDTO = new OperationResponseDTO(status, messageText);
        Map<String, Object> payload = new LinkedHashMap<>();
        ApiResponseDTO apiResponseDTO = new ApiResponseDTO("TPSYS", objectMapper.writeValueAsString(payload));
        String message = objectMapper.writeValueAsString(apiResponseDTO);
        log.info(message);
        socketHandler.sendMessageToAll(message);
    }
}
