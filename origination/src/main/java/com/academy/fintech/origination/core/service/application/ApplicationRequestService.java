package com.academy.fintech.origination.core.service.application;

import com.academy.fintech.origination.core.service.application.db.application.Application;
import com.academy.fintech.origination.core.service.application.db.application.ApplicationStatus;
import com.academy.fintech.origination.core.service.application.db.client.Client;
import com.academy.fintech.origination.core.service.application.domain_service.ApplicationOperationService;
import com.academy.fintech.origination.core.service.application.domain_service.ClientOperationService;
import com.academy.fintech.origination.grpc.service.application.v1.dto.CancelRequestDto;
import com.academy.fintech.origination.grpc.service.application.v1.dto.CreateRequestDto;
import com.academy.fintech.origination.grpc.service.application.v1.dto.ClientDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ApplicationRequestService {

    private final ApplicationOperationService applicationOperationService;

    private final ClientOperationService clientOperationService;

    public Application createApplication(ClientDto clientDto, CreateRequestDto createRequestDto) {
        Client client = clientOperationService.findOrCreateClient(clientDto);
        return applicationOperationService.createApplication(client, createRequestDto);
    }

    public void cancelApplication(CancelRequestDto dto) {
        applicationOperationService.setApplicationStatus(dto.applicationId(), ApplicationStatus.CANCELED);
    }
}
