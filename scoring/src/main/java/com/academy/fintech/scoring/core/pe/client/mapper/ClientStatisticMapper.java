package com.academy.fintech.scoring.core.pe.client.mapper;

import com.academy.fintech.scoring.application_processing.ClientStatisticRequest;
import com.academy.fintech.scoring.application_processing.ClientStatisticResponse;
import com.academy.fintech.scoring.core.processing.model.ClientStatistic;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ClientStatisticMapper {

    ClientStatistic toModel(ClientStatisticResponse response);

    ClientStatisticRequest toRequest(String clientId);

}
