package com.academy.fintech.origination.core.service.application.db.dwh.message;

import com.academy.fintech.origination.core.service.application.db.application.Application;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.type.DateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DwhMessageService {

    private final DwhMessageRepository dwhMessageRepository;

    public List<DwhMessage> findAllByStatus(RetryStatus status) {
        return dwhMessageRepository.findAllByStatus(status);
    }

    public DwhMessage createDwhMessage(String message) {
        DwhMessage dwhMessage = DwhMessage.builder()
                .message(message)
                .inserted(LocalDateTime.now())
                .status(RetryStatus.RETRY)
                .build();
        return dwhMessageRepository.save(dwhMessage);
    }

    public void save(DwhMessage dwhMessage) {
        dwhMessageRepository.save(dwhMessage);
    }

}
