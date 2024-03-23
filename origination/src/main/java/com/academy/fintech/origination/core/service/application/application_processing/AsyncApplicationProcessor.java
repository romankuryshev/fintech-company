package com.academy.fintech.origination.core.service.application.application_processing;

import com.academy.fintech.origination.core.email.EmailClientService;
import com.academy.fintech.origination.core.pe.client.ProductEngineClientService;
import com.academy.fintech.origination.core.pg.client.PaymentGateClientService;
import com.academy.fintech.origination.core.scoring.client.ScoringClientService;
import com.academy.fintech.origination.core.service.application.db.application.Application;
import com.academy.fintech.origination.core.service.application.db.application.ApplicationService;
import com.academy.fintech.origination.core.service.application.db.application.ApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
@AllArgsConstructor
public class AsyncApplicationProcessor {

    private final ApplicationService applicationService;

    private final EmailClientService emailClientService;

    private final ScoringClientService scoringClientService;

    private final ProductEngineClientService productEngineClientService;

    private final PaymentGateClientService paymentGateClientService;

    @Async
    public void processApplication(Application application) {
        ApplicationStatus status = scoringClientService.processApplication(application);
        application.setStatus(status);

        // TODO: add exception handling and rollback if ops fails
        if (status.equals(ApplicationStatus.ACCEPTED)) {
            UUID agreementId = productEngineClientService.createAgreement(application);
            application.setAgreementId(agreementId);
            paymentGateClientService.executePayment(application);
        }

        applicationService.save(application);
        emailClientService.sendApplicationStatusNotification(application);
    }
}
