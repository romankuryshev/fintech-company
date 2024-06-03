package com.academy.fintech.origination.core.service.application.application_processing;

import com.academy.fintech.origination.core.service.application.db.application.Application;
import com.academy.fintech.origination.core.service.application.db.application.ApplicationService;
import com.academy.fintech.origination.core.service.application.db.application.ApplicationStatus;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ApplicationProcessingScheduler {

    private static final int SCHEDULED_RATE = 10000;

    private final AsyncApplicationProcessor asyncApplicationProcessor;

    private final ApplicationService applicationService;

    @Scheduled(fixedRate = SCHEDULED_RATE)
    private void startScheduler() {
        List<Application> newApplications = applicationService.findAllApplicationsByStatus(ApplicationStatus.NEW);
        for(Application app : newApplications) {
            app.setStatus(ApplicationStatus.SCORING);
            applicationService.save(app);
            asyncApplicationProcessor.processApplication(app);
        }
    }
}
