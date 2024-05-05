package com.academy.fintech.origination.core.service.application.db.application;

import com.academy.fintech.origination.core.service.application.db.client.Client;
import com.academy.fintech.origination.core.service.application.dwh.sending.service.KafkaSenderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@AllArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final KafkaSenderService kafkaSenderService;

    public List<Application> findAllByClient(Client client) {
        return applicationRepository.findAllByClient(client);
    }

    @Nullable
    public Application findById(UUID id) {
        return applicationRepository.findById(id).orElse(null);
    }

    public Application save(Application application) {
        return applicationRepository.save(application);
    }

    public List<Application> findAllApplicationsByStatus(ApplicationStatus status) {
        return applicationRepository.findAllByStatus(status);
    }

    public Application findByAgreementId(UUID agreementId) {
        return applicationRepository.findByAgreementId(agreementId);
    }

    @Transactional
    public Application saveAndSendToDwh(Application application) {
        applicationRepository.save(application);
        try {
            kafkaSenderService.createMessage(application);
        } catch (JsonProcessingException e) {
            log.error("Dwh message serialization error, applicationId: {}", application.getId());
            throw new RuntimeException(e);
        }
        return application;
    }
}
