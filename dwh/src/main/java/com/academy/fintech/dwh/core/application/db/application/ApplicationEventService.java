package com.academy.fintech.dwh.core.application.db.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApplicationEventService {

    private final ApplicationEventRepository applicationEventRepository;

    public void save(ApplicationEvent applicationEvent) {
        applicationEventRepository.save(applicationEvent);
    }
}
