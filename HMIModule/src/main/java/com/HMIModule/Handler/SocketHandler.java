package com.HMIModule.Handler;

import com.HMIModule.DTO.ApiResponseDTO;
import com.HMIModule.DTO.WebSocketDTO;
import com.HMIModule.Properties.HMIProperties;
import com.HMIModule.Response.SocketResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@Component
public class SocketHandler extends TextWebSocketHandler {
    private static final SocketResponse socketResponse = new SocketResponse();

    private static final CopyOnWriteArrayList<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) throws Exception {
        try {
            sessions.add(session);
            socketResponse.sendConnectionToSocket();
        } catch (Exception e) {
            log.error(e.getMessage());
        }

    }

    @Override
    protected void handleTextMessage(@NonNull WebSocketSession session, @NonNull TextMessage message) throws Exception {
        try {
            String payload = message.getPayload();
            log.info("Received: " + payload);
            session.sendMessage(new TextMessage("Hello from server!"));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus status) throws Exception {
        try {
            sessions.remove(session);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public void sendMessageToAll(String message) {
        log.info(String.valueOf(sessions.size()));
        for (WebSocketSession session : sessions) {
            try {
                session.sendMessage(new TextMessage(message));
            } catch (Exception e) {
                log.error(e.getMessage());
                return;
            }
        }
    }
}
