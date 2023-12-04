package com.academy.fintech.pe.grpc.service.agreement.statistic.dto;

import java.math.BigDecimal;

public record AdvancedPaymentRequestDto(
        BigDecimal interest,
        int termInMonths,
        BigDecimal disbursementAmount
) {
}
