package com.academy.fintech.dwh.core.agreement;

import com.academy.fintech.dwh.core.agreement.db.agreement.AgreementEvent;
import com.academy.fintech.dwh.core.agreement.db.agreement.AgreementEventService;
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
public class AgreementEventKafkaController {

    private final AgreementEventService agreementEventService;
    private final AgreementMapper agreementMapper;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "${kafka.topic.agreement-status.name}", groupId = "dwh-agreement-1")
    public void handleMessage(@Header(KafkaHeaders.RECEIVED_KEY) String agreementId,
                               @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long timestamp,
                               @Header("idempotency_key") String eventId,
                               String message) {
        AgreementMessage agreementMessage;
        try {
           agreementMessage = objectMapper.readValue(message, AgreementMessage.class);
        } catch (JsonProcessingException e) {
            log.error("Error while try to parse message: {}", message);
            throw new RuntimeException(e);
        }

        if (agreementMessage == null) {
            return;
        }
        agreementEventService.save(createEvent(agreementMessage, agreementId, eventId, timestamp));
    }

    private AgreementEvent createEvent(AgreementMessage agreementMessage, String agreementId, String eventId, long timestamp) {
        LocalDateTime eventDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneOffset.ofHours(3));
        AgreementEvent agreementEvent = agreementMapper.toEntity(agreementMessage);

        agreementEvent.setEventId(Long.parseLong(eventId));
        agreementEvent.setEventDateTime(eventDateTime);
        agreementEvent.setEventDate(eventDateTime.toLocalDate());
        agreementEvent.setAgreementId(UUID.fromString(agreementId));

        return agreementEvent;
    }
}
