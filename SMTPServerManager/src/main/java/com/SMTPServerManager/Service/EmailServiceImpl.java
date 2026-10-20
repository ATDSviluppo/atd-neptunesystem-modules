package com.SMTPServerManager.Service;

import com.SMTPServerManager.Properties.MailProperties;
import jakarta.activation.DataHandler;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.util.ByteArrayDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(EmailServiceImpl.class);
    private static final int BATCH_SIZE = 25;

    @Autowired
    private MailProperties mailProperties;

    private record PreparedAttachment(String fileName, String contentType, byte[] data) {}

    @Override
    public ResponseEntity<String> sendMail(Map<String, Object> payload) {
        try {
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.ssl.trust", "*");
            props.put("mail.smtp.host", mailProperties.getMailServerHost());
            props.put("mail.smtp.port", mailProperties.getMailServerPort());
            props.put("mail.debug", "true");

            String port = mailProperties.getMailServerPort();
            if ("465".equals(port)) {
                props.put("mail.smtp.ssl.enable", "true");
                props.put("mail.smtp.starttls.enable", "false");
                props.put("mail.smtp.socketFactory.port", "465");
                props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                props.put("mail.smtp.socketFactory.fallback", "false");
            } else {
                props.put("mail.smtp.starttls.enable", "true");
            }

            String sender = mailProperties.getMailSender();
            String password = mailProperties.getMailPassword();

            if (!payload.containsKey("Body") || !payload.containsKey("Subject") || !payload.containsKey("ToAddress")) {
                return ResponseEntity.status(400).body("Payload malformed");
            }

            String body = (String) payload.get("Body");
            String subject = (String) payload.get("Subject");
            List<String> toAddress = (List<String>) payload.get("ToAddress");

            if (toAddress == null || toAddress.isEmpty()) {
                return ResponseEntity.status(400).body("Nessun destinatario specificato");
            }

            List<InternetAddress> addressList = new ArrayList<>();
            for (String addr : toAddress) {
                if (addr != null && !addr.trim().isEmpty()) {
                    addressList.add(new InternetAddress(addr.trim()));
                }
            }

            if (addressList.isEmpty()) {
                return ResponseEntity.status(400).body("Nessun indirizzo email valido trovato");
            }

            Session session = Session.getInstance(props, new Authenticator() {
                protected jakarta.mail.PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(sender, password);
                }
            });

            // Prepara gli allegati una sola volta per tutti i batch
            Object attachmentsObj = payload.get("Attachments");
            if (attachmentsObj == null) {
                attachmentsObj = payload.get("attachments");
            }

            List<PreparedAttachment> preparedAttachments = new ArrayList<>();
            if (attachmentsObj instanceof List) {
                List<?> attachmentsList = (List<?>) attachmentsObj;
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
                            preparedAttachments.add(new PreparedAttachment(
                                fileName != null ? fileName : "attachment",
                                contentType,
                                fileBytes
                            ));
                        }
                    }
                }
            }

            // Invio a scaglioni (batch da 25 destinatari in CCN/BCC per evitare limiti SMTP come 452 Too many recipients)
            int totalRecipients = addressList.size();
            int totalBatches = (int) Math.ceil((double) totalRecipients / BATCH_SIZE);
            log.info("Invio email a {} destinatari suddivisi in {} batch da massimo {}", totalRecipients, totalBatches, BATCH_SIZE);

            for (int i = 0; i < totalRecipients; i += BATCH_SIZE) {
                int end = Math.min(i + BATCH_SIZE, totalRecipients);
                List<InternetAddress> batch = addressList.subList(i, end);
                int currentBatchNum = (i / BATCH_SIZE) + 1;

                log.info("Invio batch {}/{} con {} destinatari in CCN", currentBatchNum, totalBatches, batch.size());

                MimeMessage message = new MimeMessage(session);
                message.setFrom(new InternetAddress(sender, "Neptune System"));
                message.addRecipients(Message.RecipientType.BCC, batch.toArray(new InternetAddress[0]));
                message.addRecipients(Message.RecipientType.TO, "no-reply@zcsautomation.com");
                message.setSubject(subject);

                if (!preparedAttachments.isEmpty()) {
                    MimeMultipart multipart = new MimeMultipart();

                    // Testo della mail
                    MimeBodyPart textPart = new MimeBodyPart();
                    textPart.setText(body, "UTF-8");
                    multipart.addBodyPart(textPart);

                    // Allegati
                    for (PreparedAttachment att : preparedAttachments) {
                        ByteArrayDataSource dataSource = new ByteArrayDataSource(att.data(), att.contentType());
                        MimeBodyPart attachmentPart = new MimeBodyPart();
                        attachmentPart.setDataHandler(new DataHandler(dataSource));
                        attachmentPart.setFileName(att.fileName());
                        multipart.addBodyPart(attachmentPart);
                    }
                    message.setContent(multipart);
                } else {
                    message.setText(body, "UTF-8");
                }

                Transport.send(message);

                if (end < totalRecipients) {
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException ignored) {
                        Thread.currentThread().interrupt();
                    }
                }
            }

            log.info("Completato con successo invio di tutti i {} batch per {} destinatari", totalBatches, totalRecipients);

        } catch (MessagingException e) {
            log.error("Errore durante l'invio della mail: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body("Errore durante l'invio " + e);
        } catch (UnsupportedEncodingException e) {
            log.error("Errore codifica sender: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok("Email inviate con successo");
    }
}
