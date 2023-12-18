package com.academy.fintech.scoring.core.processing.model;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record Product(
        String code,
        int minTermInMonths,
        int maxTermInMonths,
        BigDecimal minPrincipalAmount,
        BigDecimal maxPrincipalAmount,
        BigDecimal minInterest,
        BigDecimal maxInterest
) {
}
