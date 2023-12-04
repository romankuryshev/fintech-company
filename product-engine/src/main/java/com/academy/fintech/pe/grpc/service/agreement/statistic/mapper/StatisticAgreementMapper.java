package com.academy.fintech.pe.grpc.service.agreement.statistic.mapper;

import com.academy.fintech.pe.core.service.agreement.db.agreement.Agreement;
import com.academy.fintech.scoring.application_processing.AgreementResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface StatisticAgreementMapper {

    @Mapping(target = "agreementId", source = "id")
    AgreementResponse toResponse(Agreement agreement);
}
