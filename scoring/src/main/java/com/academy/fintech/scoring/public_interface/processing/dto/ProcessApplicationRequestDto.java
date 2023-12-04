package com.academy.fintech.scoring.public_interface.processing.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record ProcessApplicationRequestDto (
        UUID applicationId,
        UUID clientId,
        BigDecimal clientSalary,
        BigDecimal disbursementAmount
)
{ }
