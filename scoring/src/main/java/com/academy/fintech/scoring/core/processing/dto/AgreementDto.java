package com.academy.fintech.scoring.core.processing.dto;

import java.time.LocalDate;
import java.util.UUID;

public record AgreementDto(
        UUID agreementId,
        int countExpiredPayments
) {
}
