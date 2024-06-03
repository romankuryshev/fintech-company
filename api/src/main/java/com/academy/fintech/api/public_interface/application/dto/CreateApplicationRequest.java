package com.academy.fintech.api.public_interface.application.dto;

import lombok.Builder;

@Builder
public record CreateApplicationRequest(
        int salary,
        int amount,
        String productCode,
        String termInMonths,
        String interest
) {
}
