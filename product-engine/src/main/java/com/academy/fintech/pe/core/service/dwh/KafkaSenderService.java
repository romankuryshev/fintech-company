package com.academy.fintech.pe.core.service.dwh;

import com.academy.fintech.pe.core.service.agreement.db.agreement.Agreement;
import com.academy.fintech.pe.core.service.dwh.db.DwhMessage;
import com.academy.fintech.pe.core.service.dwh.db.DwhMessageService;
import com.academy.fintech.pe.core.service.dwh.db.RetryStatus;
import com.academy.fintech.pe.core.service.dwh.dto.AgreementMessage;
import com.academy.fintech.pe.grpc.service.agreement.agreement.mapper.AgreementMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class KafkaSenderService {

    private static final int SCHEDULE_RATE = 10000;
    private static final int RETRY_LIMIT = 6;
    @Value("${product-engine.kafka.topic.agreement-status.name}")
    private String topicName;

    private final ObjectMapper objectMapper;
    private final AgreementMapper agreementMapper;
    private final DwhMessageService dwhMessageService;

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void createMessage(Agreement agreement) throws JsonProcessingException {
        AgreementMessage message = agreementMapper.toMessage(agreement);
        dwhMessageService.createDwhMessage(objectMapper.writeValueAsString(message));
    }

    @Scheduled(fixedRate = SCHEDULE_RATE)
    void sendMessage() {
        dwhMessageService.findAllByStatus(RetryStatus.RETRY)
                .forEach(this::send);
    }

    void send(DwhMessage dwhMessage) {
        CompletableFuture<SendResult<String, String>> result = kafkaTemplate
                .send(topicName, Long.toString(dwhMessage.getId()), dwhMessage.getMessage());

        result.whenComplete((res, err) -> {
            if (err == null) {
                dwhMessage.setStatus(RetryStatus.SUCCESS);
            } else if (dwhMessage.getCounter() < RETRY_LIMIT) {
                dwhMessage.incrementCounter();
            } else {
                dwhMessage.setStatus(RetryStatus.FAIL);
            }
            dwhMessageService.save(dwhMessage);
        });
    }
}
