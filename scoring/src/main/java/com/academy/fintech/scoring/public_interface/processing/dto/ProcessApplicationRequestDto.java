package com.academy.fintech.scoring.public_interface.processing.dto;

import java.math.BigDecimal;

public record ProcessApplicationRequestDto (
        String applicationId,
        String clientId,
        BigDecimal clientSalary,
        BigDecimal disbursementAmount
)
{ }
