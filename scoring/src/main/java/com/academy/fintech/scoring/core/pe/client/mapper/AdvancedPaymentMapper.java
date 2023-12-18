package com.academy.fintech.scoring.core.pe.client.mapper;

import com.academy.fintech.scoring.application_processing.AdvancedPaymentRequest;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.math.BigDecimal;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface AdvancedPaymentMapper {

    AdvancedPaymentRequest toRequest(BigDecimal interest, int term, BigDecimal disbursementAmount);
}
