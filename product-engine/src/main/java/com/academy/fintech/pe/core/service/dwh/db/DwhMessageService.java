package com.academy.fintech.pe.core.service.dwh.db;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DwhMessageService {

    private final DwhMessageRepository dwhMessageRepository;

    public List<DwhMessage> findAllByStatus(RetryStatus status) {
        return dwhMessageRepository.findAllByStatus(status);
    }

    public DwhMessage createDwhMessage(UUID key, String message) {
        DwhMessage dwhMessage = DwhMessage.builder()
                .key(key)
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
