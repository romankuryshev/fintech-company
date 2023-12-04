package com.academy.fintech.pe.grpc.service.agreement.statistic.mapper;

import com.academy.fintech.pe.grpc.service.agreement.statistic.dto.AdvancedPaymentRequestDto;
import com.academy.fintech.scoring.application_processing.AdvancedPaymentRequest;
import com.academy.fintech.scoring.application_processing.AdvancedPaymentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.math.BigDecimal;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface AdvancedPaymentMapper {

    @Mapping(target = "termInMonths", source = "term")
    AdvancedPaymentRequestDto toDto(AdvancedPaymentRequest request);

    AdvancedPaymentResponse toResponse(BigDecimal paymentAmount);
}
