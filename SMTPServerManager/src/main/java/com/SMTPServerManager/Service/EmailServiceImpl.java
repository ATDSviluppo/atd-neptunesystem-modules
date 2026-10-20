package com.SMTPServerManager.Service;

import com.SMTPServerManager.Properties.MailProperties;
import jakarta.activation.DataHandler;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.util.ByteArrayDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    private MailProperties mailProperties;

    @Override
    public ResponseEntity<String> sendMail(Map<String, Object> payload) {
        try {
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.ssl.trust", "*");
            props.put("mail.smtp.host", mailProperties.getMailServerHost());
            props.put("mail.smtp.port", mailProperties.getMailServerPort());
            props.put("mail.debug", "true");

            String sender = mailProperties.getMailSender();
            String password = mailProperties.getMailPassword();

            if (!payload.containsKey("Body") || !payload.containsKey("Subject") || !payload.containsKey("ToAddress")) {
                return ResponseEntity.status(400).body("Payload malformed");
            }

            String body = (String) payload.get("Body");
            String subject = (String) payload.get("Subject");
            List<String> toAddress = (List<String>) payload.get("ToAddress");

            Session session = Session.getInstance(props, new Authenticator() {
                protected jakarta.mail.PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(sender, password);
                }
            });

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(sender, "Neptune System"));
            List<InternetAddress> addressList = new ArrayList<>();
            for (String addr : toAddress) {
                if (addr != null && !addr.trim().isEmpty()) {
                    addressList.add(new InternetAddress(addr.trim()));
                }
            }
            if (!addressList.isEmpty()) {
                message.addRecipients(Message.RecipientType.BCC, addressList.toArray(new InternetAddress[0]));
            }
            message.addRecipients(Message.RecipientType.TO, "no-reply@zcsautomation.com");
            message.setSubject(subject);

            Object attachmentsObj = payload.get("Attachments");
            if (attachmentsObj == null) {
                attachmentsObj = payload.get("attachments");
            }

            if (attachmentsObj instanceof List) {
                List<?> attachmentsList = (List<?>) attachmentsObj;
                if (!attachmentsList.isEmpty()) {
                    MimeMultipart multipart = new MimeMultipart();

                    // Testo della mail
                    MimeBodyPart textPart = new MimeBodyPart();
                    textPart.setText(body, "UTF-8");
                    multipart.addBodyPart(textPart);

                    // Allegati
                    for (Object item : attachmentsList) {
                        if (item instanceof Map) {
                            Map<?, ?> attMap = (Map<?, ?>) item;
                            String fileName = (String) (attMap.get("FileName") != null ? attMap.get("FileName") : attMap.get("fileName"));
                            String contentType = (String) (attMap.get("ContentType") != null ? attMap.get("ContentType") : attMap.get("contentType"));
                            if (contentType == null || contentType.isBlank()) {
                                contentType = "application/octet-stream";
                            }
                            String base64Data = (String) (attMap.get("Data") != null ? attMap.get("Data") : attMap.get("data"));

                            if (base64Data != null && !base64Data.isBlank()) {
                                byte[] fileBytes = Base64.getDecoder().decode(base64Data);
                                ByteArrayDataSource dataSource = new ByteArrayDataSource(fileBytes, contentType);
                                MimeBodyPart attachmentPart = new MimeBodyPart();
                                attachmentPart.setDataHandler(new DataHandler(dataSource));
                                attachmentPart.setFileName(fileName != null ? fileName : "attachment");
                                multipart.addBodyPart(attachmentPart);
                            }
                        }
                    }
                    message.setContent(multipart);
                } else {
                    message.setText(body);
                }
            } else {
                message.setText(body);
            }

            Transport.send(message);
        } catch (MessagingException e) {
           return ResponseEntity.status(500).body("Errore durante l'invio " + e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok("Email inviate con successo");

    }
}
