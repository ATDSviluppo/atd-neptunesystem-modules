package com.SMTPServerManager.Service;

import com.SMTPServerManager.Properties.MailProperties;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
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
            props.put("mail.smtp.starttls.enable", "false");
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
            InternetAddress[] address = new InternetAddress[toAddress.size()];
            for (int i = 0; i < toAddress.size(); i++) {
                address[i] = new InternetAddress(toAddress.get(i));
            }
            message.addRecipients(Message.RecipientType.BCC, address);
            message.addRecipients(Message.RecipientType.TO, "no-reply@zcsautomation.com");
            message.setSubject(subject);
            message.setText(body);
            message.setFrom(new InternetAddress(sender));
            Transport.send(message);
        } catch (MessagingException e) {
           return ResponseEntity.status(500).body("Errore durante l'invio " + e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok("Email inviate con successo");

    }
}
