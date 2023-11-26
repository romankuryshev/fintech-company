package com.academy.fintech.origination.grpc.service.application.v1.mapper;

import com.academy.fintech.application.CreateRequest;
import com.academy.fintech.origination.grpc.service.application.v1.dto.ClientDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ClientMapper {

    ClientDto toDto(CreateRequest request);
}
