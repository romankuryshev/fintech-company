package com.academy.fintech.pe.grpc.service.agreement.agreement.mapper;

import com.academy.fintech.pe.core.service.agreement.db.agreement.Agreement;
import com.academy.fintech.pe.core.service.dwh.dto.AgreementMessage;
import com.academy.fintech.pe.grpc.service.agreement.agreement.AgreementRequest;
import com.academy.fintech.pe.grpc.service.agreement.agreement.AgreementResponse;
import com.academy.fintech.pe.grpc.service.agreement.agreement.dto.AgreementDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.math.BigDecimal;

@Mapper(componentModel = "spring", imports = BigDecimal.class, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AgreementMapper {
    @Mapping(target = "principalAmount",  expression = "java( new BigDecimal(request.getOriginationAmount())" +
            ".add(new BigDecimal(request.getDisbursementAmount())))")
    AgreementDto requestToDto(AgreementRequest request);

    AgreementResponse toResponse(Agreement agreement);

    @Mapping(target = "productCode", source = "product.code")
    AgreementMessage toMessage(Agreement agreement);
}
