package com.academy.fintech.dwh.core.application;

import com.academy.fintech.dwh.core.application.db.application.ApplicationEvent;
import com.academy.fintech.dwh.core.application.db.application.ApplicationEventService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class ApplicationEvenKafkaController {

    private final ObjectMapper objectMapper;
    private final ApplicationMapper applicationMapper;
    private final ApplicationEventService applicationEventService;

    @KafkaListener(topics = "${kafka.topic.application-creation.name}", groupId = "dwh-application-1")
    public void handleMessage(@Header(KafkaHeaders.RECEIVED_KEY) String applicationId,
                               @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long timestamp,
                               @Header("idempotency_key") String eventId,
                               String message) {
        ApplicationMessage applicationMessage;
        try {
            applicationMessage = objectMapper.readValue(message, ApplicationMessage.class);
        } catch (JsonProcessingException e) {
            log.error("Error while try to parse message: {}", message);
            throw new RuntimeException(e);
        }

        if (applicationMessage == null) {
            return;
        }
        applicationEventService.save(createEvent(applicationMessage, applicationId, eventId, timestamp));
    }

    private ApplicationEvent createEvent(ApplicationMessage applicationMessage, String applicationId, String eventId, long timestamp) {
        LocalDateTime eventDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneOffset.ofHours(3));
        ApplicationEvent applicationEvent = applicationMapper.toEntity(applicationMessage);

        applicationEvent.setEventDateTime(eventDateTime);
        applicationEvent.setEventId(Long.parseLong(eventId));
        applicationEvent.setEventDate(eventDateTime.toLocalDate());
        applicationEvent.setApplicationId(UUID.fromString(applicationId));
        return applicationEvent;
    }
}