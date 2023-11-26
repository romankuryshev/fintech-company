package com.academy.fintech.origination.grpc.service.application.v1.mapper;

import com.academy.fintech.application.CreateRequest;
import com.academy.fintech.application.CreateResponse;
import com.academy.fintech.application.RemoveRequest;
import com.academy.fintech.application.RemoveResponse;
import com.academy.fintech.origination.core.service.application.db.application.Application;
import com.academy.fintech.origination.grpc.service.application.v1.dto.CreateRequestDto;
import com.academy.fintech.origination.grpc.service.application.v1.dto.RemoveRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ApplicationMapper {

    CreateRequestDto toDto(CreateRequest request);

    @Mapping(target = "applicationId", source = "id")
    CreateResponse toResponse(Application application);

    RemoveRequestDto toDto(RemoveRequest request);

    RemoveResponse toResponse(boolean success, String message);
}
