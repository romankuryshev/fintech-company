package com.academy.fintech.scoring.core.processing.model;

import java.math.BigDecimal;

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
