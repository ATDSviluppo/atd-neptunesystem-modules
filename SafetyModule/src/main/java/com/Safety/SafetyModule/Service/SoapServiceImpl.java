package com.Safety.SafetyModule.Service;

import com.AuthenticationModule.Repository.DeviceRepository;
import com.AuthenticationModule.Repository.EventRepository;
import com.CommonModule.CommonModule.Entity.Device;
import com.Safety.SafetyModule.Entity.EventToSend;
import com.Safety.SafetyModule.Properties.SoapProperties;
import com.Safety.SafetyModule.Repository.SoapRepository;
import com.CommonModule.CommonModule.Entity.Event;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import jakarta.xml.soap.MessageFactory;
import jakarta.xml.soap.SOAPMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
public class SoapServiceImpl {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private SoapRepository soapRepository;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private SoapProperties soapProperties;

    private Long timeToScanEventTable;

    @PostConstruct
    private void initialize() {
        final Long timeToScanEventTable = soapProperties.getTimeToScanEventTable();
    }

    public void saveEventToSend() {
        List<Event> events = eventRepository.findEventsToSave();

        if (!events.isEmpty()) {
            for (Event event : events) {
                EventToSend eventToSend = new EventToSend();
                SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SS");

                String formattedTimestamp = formatter.format(event.getEventTimestamp());

                eventToSend.setEventTimestamp(formattedTimestamp);
                eventToSend.setEventType(event.getEventType());
                eventToSend.setDeviceId(event.getDeviceId());
                eventToSend.setDeviceDetailId(eventToSend.getDeviceDetailId());
                eventToSend.setNote(event.getNote());
                eventToSend.setEnumDeviceTypeId(event.getEnumDeviceTypeId());
                eventToSend.setEmployeeId(event.getEmployeeId());
                eventToSend.setLocation(event.getLocation());
                eventToSend.setMachineId(event.getMachineId());
                eventToSend.setPlanningId(event.getPlanningId());
                eventToSend.setDeviceBarCode("?");

                soapRepository.save(eventToSend);

                eventRepository.updateEventSent(event.getEventId());
            }
        } else {
            log.info("nessun evento presente");
        }

    }

    //@Scheduled(fixedRateString = "${timeToScanEventTable}")
    @Scheduled(fixedRate = 300000)
    @Transactional
    public void sendSoapRequest() throws Exception {
        log.info("Scansione di eventtosend iniziata...");
        if (soapProperties == null || soapProperties.getUrlWeb().isBlank() ||
                soapProperties.getUserName().isBlank() || soapProperties.getPassword().isBlank() ||
                soapProperties.getCompany().isBlank()) {
            throw new IllegalArgumentException("Le impostazioni SOAP non sono valide.");
        }

        saveEventToSend();

        List<EventToSend> eventToSend = soapRepository.findAll();

        for (EventToSend event : eventToSend) {
            SOAPMessage soapMessage = createPayloadSoap(event);
            HttpStatus result = sendSoapMessage(soapMessage, soapProperties.getUrlWeb());

            if (result == HttpStatus.OK) {
                soapRepository.delete(event);
            }
        }
    }

    @Transactional
    public String createJsonPayload(EventToSend eventToSend) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        Device device = deviceRepository.getReferenceById(eventToSend.getDeviceId());

        Map<String, String> data = new HashMap<>();

        data.put("DeviceBarCode", device.getDeviceBarCode());
        data.put("DeviceDetailIdDpiSize", device.getDeviceDetail());
        data.put("DeviceId", device.getDeviceId());
        data.put("DeviceTypeIdDpiType", eventToSend.getEnumDeviceTypeId());
        data.put("EmployeeId", eventToSend.getEmployeeId());
        data.put("EventTimestamp", eventToSend.getEventTimestamp());
        data.put("EventType", eventToSend.getEventType());
        data.put("Location", eventToSend.getLocation());
        data.put("MachineId", eventToSend.getMachineId());
        data.put("Note", eventToSend.getNote());

        List<Map<String, String>> dataList = Collections.singletonList(data);

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("idType", "Event");
        payload.put("version", 1.0);
        payload.put("data", dataList);

        return objectMapper.writeValueAsString(payload);
    }


    public SOAPMessage createPayloadSoap(EventToSend eventToSend) {
        try {
            String soapEnvelope = """
                        <soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/' xmlns:hsba='http://hsba_fdispenserws.ws.localhost/'>
                            <soapenv:Header/>
                            <soapenv:Body>
                                <hsba:hsba_fdispenserws_Run>
                                    <hsba:m_UserName>%s</hsba:m_UserName>
                                    <hsba:m_Password>%s</hsba:m_Password>
                                    <hsba:m_Company>%s</hsba:m_Company>
                                    <hsba:pPAYLOAD>%s</hsba:pPAYLOAD>
                                </hsba:hsba_fdispenserws_Run>
                            </soapenv:Body>
                        </soapenv:Envelope>
                    """.formatted(
                    soapProperties.getUserName(),
                    soapProperties.getPassword(),
                    soapProperties.getCompany(),
                    createJsonPayload(eventToSend)
            );

            log.info(soapEnvelope);

            MessageFactory messageFactory = MessageFactory.newInstance();
            ByteArrayInputStream inputStream = new ByteArrayInputStream(soapEnvelope.getBytes(StandardCharsets.UTF_8));
            return messageFactory.createMessage(null, inputStream);
        } catch (Exception e) {
            throw new RuntimeException("Errore nella creazione del payload SOAP", e);
        }
    }

    private HttpStatus sendSoapMessage(SOAPMessage soapMessage, String urlWeb) throws Exception {
        URL url = new URL(urlWeb);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
        connection.setDoOutput(true);
        connection.setDoInput(true);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            soapMessage.writeTo(outputStream);
            byte[] soapBytes = outputStream.toByteArray();
            connection.getOutputStream().write(soapBytes);
        }

        int responseCode = connection.getResponseCode();
        log.info("HTTP Response Status Code: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (InputStream inputStream = connection.getInputStream()) {
                // Legge il contenuto come stringa (per debug/log)
                String responseXml = new BufferedReader(new InputStreamReader(inputStream))
                        .lines()
                        .reduce("", (acc, line) -> acc + line + "\n");

                log.info("SOAP Response Content:\n" + responseXml);

                if (responseXml.contains("<return>OK</return>")) {
                    log.info("Risposta SOAP: OK");
                } else if (responseXml.contains("<return>KO</return>")) {
                    log.info("Risposta SOAP: KO");
                    return HttpStatus.INTERNAL_SERVER_ERROR;
                } else {
                    log.info("Risposta SOAP non riconosciuta: ");
                }

                return HttpStatus.OK;
            }
        } else {
            try (InputStream errorStream = connection.getErrorStream()) {
                if (errorStream != null) {
                    String errorBody = new BufferedReader(new InputStreamReader(errorStream))
                            .lines()
                            .reduce("", (acc, line) -> acc + line + "\n");
                    log.info("Errore nella risposta SOAP. Body: " + errorBody);
                }
            }
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }
}
