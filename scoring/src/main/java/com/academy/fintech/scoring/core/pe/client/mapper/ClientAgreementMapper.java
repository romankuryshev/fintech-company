package com.academy.fintech.scoring.core.pe.client.mapper;

import com.academy.fintech.scoring.application_processing.AgreementResponse;
import com.academy.fintech.scoring.application_processing.ClientAgreementsRequest;
import com.academy.fintech.scoring.core.processing.model.AgreementDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        uses = AgreementMapper.class)
public interface ClientAgreementMapper {

    List<AgreementDto> toModel(List<AgreementResponse> responses);

    ClientAgreementsRequest toRequest(UUID clientId);

}
