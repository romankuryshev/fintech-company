package com.academy.fintech.pe.grpc.service.agreement.statistic.mapper;

import com.academy.fintech.pe.grpc.service.agreement.statistic.dto.ClientAgreementDto;
import com.academy.fintech.scoring.application_processing.ClientAgreementsResponse;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        uses = StatisticAgreementMapper.class)
public interface ClientAgreementsMapper {

    @Mapping(target = "agreementsList", source = "agreements")
    ClientAgreementsResponse toResponse(ClientAgreementDto agreementDto);
}
