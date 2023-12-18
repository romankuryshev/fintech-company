package com.academy.fintech.scoring.core.pe.client.mapper;

import com.academy.fintech.scoring.application_processing.AgreementResponse;
import com.academy.fintech.scoring.core.processing.dto.AgreementDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AgreementMapper {

    AgreementDto toDto(AgreementResponse agreementResponse);
}
