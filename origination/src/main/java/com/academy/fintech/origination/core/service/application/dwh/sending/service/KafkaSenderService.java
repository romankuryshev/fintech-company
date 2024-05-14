package com.academy.fintech.origination.core.service.application.dwh.sending.service;

import com.academy.fintech.origination.core.service.application.db.application.Application;
import com.academy.fintech.origination.core.service.application.db.dwh.message.DwhMessage;
import com.academy.fintech.origination.core.service.application.db.dwh.message.DwhMessageService;
import com.academy.fintech.origination.core.service.application.db.dwh.message.RetryStatus;
import com.academy.fintech.origination.core.service.application.dwh.sending.service.dto.ApplicationMessage;
import com.academy.fintech.origination.grpc.service.application.v1.mapper.ApplicationMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class KafkaSenderService {

    private static final int SCHEDULE_RATE = 10000;
    private static final int RETRY_LIMIT = 6;
    private static final String HEADER_IDEMPOTENCY_KEY = "idempotency_key";

    private final ObjectMapper objectMapper;
    private final DwhMessageService dwhMessageService;
    private final ApplicationMapper applicationMapper;

    private final TopicInformation topicInformation;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void createMessage(Application application) throws JsonProcessingException {
        ApplicationMessage message = applicationMapper.toMessage(application);
        dwhMessageService.createDwhMessage(application.getId(), objectMapper.writeValueAsString(message));
    }

    @Scheduled(fixedRate = SCHEDULE_RATE)
    void sendMessage() {
        dwhMessageService.findAllByStatus(RetryStatus.RETRY)
                .forEach(this::send);
    }

    void send(DwhMessage dwhMessage) {
        CompletableFuture<SendResult<String, String>> result = kafkaTemplate
                .send(createProducerRecord(dwhMessage));

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

    private ProducerRecord<String, String> createProducerRecord(DwhMessage dwhMessage) {
        List<Header> headers = List.of(new RecordHeader(HEADER_IDEMPOTENCY_KEY, dwhMessage.getId().toString().getBytes()));
        return new ProducerRecord<>(topicInformation.getName(),
                dwhMessage.getKey().hashCode() % topicInformation.getPartitionCount(),
                dwhMessage.getKey().toString(),
                dwhMessage.getMessage(),
                headers);
    }
}
