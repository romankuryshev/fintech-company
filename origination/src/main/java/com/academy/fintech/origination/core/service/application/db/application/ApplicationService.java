package com.academy.fintech.origination.core.service.application.db.application;

import com.academy.fintech.origination.core.service.application.db.client.Client;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;

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
}
