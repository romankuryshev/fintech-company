package com.academy.fintech.origination.core.service.application.dwh.sending.service;

import com.academy.fintech.origination.core.service.application.db.application.Application;
import com.academy.fintech.origination.core.service.application.db.dwh.message.DwhMessage;
import com.academy.fintech.origination.core.service.application.db.dwh.message.DwhMessageService;
import com.academy.fintech.origination.core.service.application.db.dwh.message.RetryStatus;
import com.academy.fintech.origination.core.service.application.dwh.sending.service.dto.ApplicationMessage;
import com.academy.fintech.origination.core.service.application.dwh.sending.service.mapper.ApplicationMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class KafkaSenderService {

    private static final int SCHEDULE_RATE = 10000;
    private static final int RETRY_LIMIT = 6;

    private final ObjectMapper objectMapper;
    private final DwhMessageService dwhMessageService;
    private final ApplicationMapper applicationMapper;

    private final TopicInformation topicInformation;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void createMessage(Application application) throws JsonProcessingException {
        ApplicationMessage message = applicationMapper.toMessage(application);
        dwhMessageService.createDwhMessage(objectMapper.writeValueAsString(message));
    }

    @Scheduled(fixedRate = SCHEDULE_RATE)
    void sendMessage() {
        dwhMessageService.findAllByStatus(RetryStatus.RETRY)
                .forEach(this::send);
    }

    void send(DwhMessage dwhMessage) {
        CompletableFuture<SendResult<String, String>> result = kafkaTemplate
                .send(topicInformation.getName(), Long.toString(dwhMessage.getId()), dwhMessage.getMessage());

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
