package com.academy.fintech.origination.grpc.service.application.v1.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CreateRequestDto(

        @NotNull
        @Positive
        BigDecimal disbursementAmount,
        @NotNull
        String productCode,
        @NotNull
        int termInMonths,
        @NotNull
        String interest
) {
}
