package com.academy.fintech.origination.core.service.application.application_processing;

import com.academy.fintech.origination.core.service.application.db.application.Application;
import com.academy.fintech.origination.core.service.application.db.application.ApplicationService;
import com.academy.fintech.origination.core.service.application.db.application.ApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ApplicationProcessingScheduler {

    private static final int scheduledRate = 10000;

    private final AsyncApplicationProcessor asyncApplicationProcessor;

    private final ApplicationService applicationService;

    @Scheduled(fixedRate = scheduledRate)
    private void startScheduler() {
        List<Application> newApplications = applicationService.findAllApplicationsByStatus(ApplicationStatus.NEW);
        for(Application app : newApplications) {
            asyncApplicationProcessor.processApplication(app);
        }
    }
}
