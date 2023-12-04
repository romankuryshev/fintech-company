package com.academy.fintech.origination.core.service.application.application_processing;

import com.academy.fintech.origination.core.scoring.client.ScoringClientService;
import com.academy.fintech.origination.core.service.application.db.application.Application;
import com.academy.fintech.origination.core.service.application.db.application.ApplicationService;
import com.academy.fintech.origination.core.service.application.db.application.ApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class AsyncApplicationProcessor {

    private final ApplicationService applicationService;

    private final ScoringClientService scoringClientService;

    @Async
    public void processApplication(Application application) {
        log.info("processing application " + application.getId());
        ApplicationStatus status = scoringClientService.processApplication(application);
        application.setStatus(status);
        applicationService.save(application);
    }
}
