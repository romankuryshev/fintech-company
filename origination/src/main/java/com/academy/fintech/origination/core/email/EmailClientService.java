package com.academy.fintech.origination.core.email;

import com.academy.fintech.origination.core.service.application.db.application.Application;
import com.academy.fintech.origination.core.service.application.db.client.Client;
import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmailClientService {
    private static final String STATUS_NOTIFICATION_TEXT = "%s %s, Your application №%s has been reviewed" +
            " and it's status has been changed.\n" +
            "Current status of your application: %s\n";

    private static final String FROM_ADDRESS = "noreply@fintech.com";

    private static final String SUBJECT = "Application status updated.";

    private final EmailClient emailClient;

    public void sendApplicationStatusNotification(Application application) {
        Client client = application.getClient();
        String messageText = String.format(STATUS_NOTIFICATION_TEXT, client.getFirstName(), client.getLatName(), application.getId(), application.getStatus());

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(FROM_ADDRESS);
        message.setSubject(SUBJECT);
        message.setTo(client.getEmail());
        message.setText(messageText);

        emailClient.sendSimpleMessage(message);
    }
}
