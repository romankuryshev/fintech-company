package com.academy.fintech.api.core.origination.client.dto;

import lombok.Builder;

@Builder
public record OriginationCreateRequest(
        String email,
        int salary,
        int amount,
        String productCode,
        String termInMonths,
        String interest
) {
}
