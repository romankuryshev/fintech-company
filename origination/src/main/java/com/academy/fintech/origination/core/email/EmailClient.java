package com.academy.fintech.origination.core.email;

import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class EmailClient {

    private JavaMailSender emailSender;

    public void sendSimpleMessage(SimpleMailMessage message) {
//        emailSender.send(message);
    }
}