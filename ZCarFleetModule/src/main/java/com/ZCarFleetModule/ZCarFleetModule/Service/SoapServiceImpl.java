package com.ZCarFleetModule.ZCarFleetModule.Service;

import com.AuthenticationModule.Repository.EventRepository;
import com.CommonModule.CommonModule.Entity.Event;
import com.ZCarFleetModule.ZCarFleetModule.Entity.EventToSend;
import com.ZCarFleetModule.ZCarFleetModule.Properties.SoapProperties;
import com.ZCarFleetModule.ZCarFleetModule.Repository.SoapRepository;
import jakarta.annotation.PostConstruct;
import jakarta.xml.soap.MessageFactory;
import jakarta.xml.soap.SOAPMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.List;

@Service
@Slf4j
public class SoapServiceImpl {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private SoapRepository soapRepository;

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
    @Scheduled(fixedRate = 5000)
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


    public SOAPMessage createPayloadSoap(EventToSend eventToSend) {
        try {
            String soapEnvelope = """
                        <soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/' xmlns:hocf='http://hocf_fzcs.ws.localhost/'>
                            <soapenv:Header/>
                            <soapenv:Body>
                                <hocf:hocf_fzcs_Run>
                                    <hocf:m_UserName>%s</hocf:m_UserName>
                                    <hocf:m_Password>%s</hocf:m_Password>
                                    <hocf:m_Company>%s</hocf:m_Company>
                                    <hocf:EventTimestamp>%s</hocf:EventTimestamp>
                                    <hocf:EventType>%s</hocf:EventType>
                                    <hocf:DeviceId>%s</hocf:DeviceId>
                                    <hocf:DeviceTypeIdDpiType>%s</hocf:DeviceTypeIdDpiType>
                                    <hocf:DeviceDetailIdDpiSize>%s</hocf:DeviceDetailIdDpiSize>
                                    <hocf:DeviceBarCode>%s</hocf:DeviceBarCode>
                                    <hocf:EmployeeId>%s</hocf:EmployeeId>
                                    <hocf:Location>%s</hocf:Location>
                                    <hocf:MachineId>%s</hocf:MachineId>
                                    <hocf:PlanningId>%s</hocf:PlanningId>
                                    <hocf:Note>%s</hocf:Note>
                                </hocf:hocf_fzcs_Run>
                            </soapenv:Body>
                        </soapenv:Envelope>
                    """.formatted(
                    soapProperties.getUserName(),
                    soapProperties.getPassword(),
                    soapProperties.getCompany(),
                    eventToSend.getEventTimestamp(),
                    eventToSend.getEventType(),
                    eventToSend.getDeviceId(),
                    eventToSend.getEnumDeviceTypeId(),
                    eventToSend.getDeviceDetailId(),
                    eventToSend.getDeviceBarCode(),
                    eventToSend.getEmployeeId(),
                    eventToSend.getLocation(),
                    eventToSend.getMachineId(),
                    eventToSend.getPlanningId(),
                    eventToSend.getNote()
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
