package com.academy.fintech.origination.core.service.application.domain_service;

import com.academy.fintech.origination.core.service.application.db.application.Application;
import com.academy.fintech.origination.core.service.application.db.application.ApplicationService;
import com.academy.fintech.origination.core.service.application.db.application.ApplicationStatus;
import com.academy.fintech.origination.core.service.application.db.client.Client;
import com.academy.fintech.origination.core.service.application.domain_service.exception.ApplicationAlreadyExistsException;
import com.academy.fintech.origination.core.service.application.domain_service.exception.ApplicationCancelingException;
import com.academy.fintech.origination.core.service.application.domain_service.exception.ApplicationNotFoundException;
import com.academy.fintech.origination.grpc.service.application.v1.dto.CreateRequestDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@AllArgsConstructor
public class ApplicationOperationService {

    private final ApplicationService applicationService;

    /**
     * Метод создает новую заявку на кредит.
     * @param client  клиент
     * @param requestDto  информация о заявке
     * @return новая заявка
     * @throws ApplicationAlreadyExistsException  если у клиента уже существует заявка с такими же данными
     */
    public Application createApplication(Client client, CreateRequestDto requestDto) {
        List<Application> clientApplications = applicationService.findAllByClient(client);

        for (var application : clientApplications) {
            if (application.getStatus() == ApplicationStatus.NEW &&
                    application.getRequestDisbursementAmount().equals(requestDto.disbursementAmount())) {

                log.error("Application already exists.");
                throw new ApplicationAlreadyExistsException("Application already exists.", application.getId());
            }
        }

        var application = Application.builder()
                .client(client)
                .status(ApplicationStatus.NEW)
                .requestDisbursementAmount(requestDto.disbursementAmount())
                .build();

        applicationService.save(application);
        return application;
    }

    public void cancelApplication(UUID applicationId) {
        Application application = applicationService.findById(applicationId);
        if (application == null) {
            log.error("application doesn't exists. id - " + applicationId);
            throw new ApplicationNotFoundException("Application with id " + applicationId + " not found.");
        }

        if (application.getStatus() != ApplicationStatus.NEW && application.getStatus() != ApplicationStatus.SCORING) {
            log.error("application with id - " + applicationId + " and status - " + application.getStatus() + " already processed.");
            throw new ApplicationCancelingException("Application with id " + applicationId + " already processed.");
        }

        application.setStatus(ApplicationStatus.CANCELED);
        applicationService.save(application);
    }
}
