package com.academy.fintech.pe.grpc.service.agreement.agreement.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record AgreementDto(
        String productCode,
        UUID clientId,
        BigDecimal interest,
        Integer term,
        BigDecimal principalAmount,
        BigDecimal originationAmount
) {
}
