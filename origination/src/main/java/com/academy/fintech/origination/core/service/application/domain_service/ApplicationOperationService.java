package com.academy.fintech.origination.core.service.application.domain_service;

import com.academy.fintech.origination.core.service.application.db.application.Application;
import com.academy.fintech.origination.core.service.application.db.application.ApplicationService;
import com.academy.fintech.origination.core.service.application.db.application.ApplicationStatus;
import com.academy.fintech.origination.core.service.application.db.client.Client;
import com.academy.fintech.origination.core.service.application.domain_service.exception.ApplicationAlreadyExistsException;
import com.academy.fintech.origination.core.service.application.domain_service.exception.ApplicationDeleteException;
import com.academy.fintech.origination.grpc.service.application.v1.dto.CreateRequestDto;
import com.academy.fintech.origination.grpc.service.application.v1.dto.RemoveRequestDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class ApplicationOperationService {

    private final ApplicationService applicationService;

    public Application createApplication(Client client, CreateRequestDto requestDto) {
        List<Application> clientApplications = applicationService.findAllByClient(client);

        for (var application : clientApplications) {
            if (application.getStatus() == ApplicationStatus.NEW &&
                    application.getRequestDisbursementAmount().equals(requestDto.disbursementAmount())) {

                log.debug("Application already exists.");
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

    public void removeApplication(RemoveRequestDto dto) {
        Application application = applicationService.findById(dto.applicationId());
        if (application == null) {
            log.debug("application doesn't exists. id - " + dto.applicationId());
            throw new ApplicationDeleteException("Application with id " + dto.applicationId() + " doesn't exists.");
        }

        if (application.getStatus() != ApplicationStatus.NEW && application.getStatus() != ApplicationStatus.SCORING) {
            log.debug("application with id - " + application.getId() + " and status - " + application.getStatus());
            throw new ApplicationDeleteException("Application with id " + dto.applicationId() + " already processed.");
        }

        applicationService.deleteById(dto.applicationId());
    }
}
