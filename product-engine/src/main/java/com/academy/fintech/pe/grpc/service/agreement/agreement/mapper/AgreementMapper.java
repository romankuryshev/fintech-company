package com.academy.fintech.pe.grpc.service.agreement.agreement.mapper;

import com.academy.fintech.pe.core.service.agreement.db.agreement.Agreement;
import com.academy.fintech.pe.grpc.service.agreement.agreement.AgreementRequest;
import com.academy.fintech.pe.grpc.service.agreement.agreement.AgreementResponse;
import com.academy.fintech.pe.grpc.service.agreement.agreement.dto.AgreementDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;

@Mapper(componentModel = "spring", imports = BigDecimal.class)
public interface AgreementMapper {
    @Mapping(target = "principalAmount",  expression = "java( new BigDecimal(request.getOriginationAmount())" +
            ".add(new BigDecimal(request.getDisbursementAmount())))")
    AgreementDto requestToDto(AgreementRequest request);

    @Mapping(target = "message", source = "message")

    AgreementResponse toResponse(Agreement agreement, String message);
}
