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
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class KafkaSenderService {

    private static final int SCHEDULE_RATE = 10000;
    private static final int RETRY_LIMIT = 6;
    private static final String HEADER_IDEMPOTENCY_KEY = "idempotency_key";

    private final ObjectMapper objectMapper;
    private final AgreementMapper agreementMapper;
    private final DwhMessageService dwhMessageService;
    private final TopicInformation agreementStatusTopic;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void createMessage(Agreement agreement) throws JsonProcessingException {
        AgreementMessage message = agreementMapper.toMessage(agreement);
        dwhMessageService.createDwhMessage(agreement.getId(), objectMapper.writeValueAsString(message));
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
        return new ProducerRecord<>(agreementStatusTopic.getName(),
                dwhMessage.getKey().hashCode() % agreementStatusTopic.getPartitionCount(),
                dwhMessage.getKey().toString(),
                dwhMessage.getMessage(),
                headers);
    }
}
