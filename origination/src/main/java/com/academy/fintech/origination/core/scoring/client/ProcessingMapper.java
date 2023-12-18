package com.academy.fintech.origination.core.scoring.client;

import com.academy.fintech.origination.core.service.application.db.application.Application;
import com.academy.fintech.origination.core.service.application.db.application.ApplicationStatus;
import com.academy.fintech.scoring.application_processing.ProcessApplicationRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProcessingMapper {

    @Mapping(target = "clientId", source = "application.client.id")
    @Mapping(target = "applicationId", source = "id")
    @Mapping(target = "clientSalary", source = "application.client.salary")
    @Mapping(target = "disbursementAmount", source = "application.requestDisbursementAmount")
    ProcessApplicationRequest toRequest(Application application);

    ApplicationStatus toApplicationStatus(String status);
}
