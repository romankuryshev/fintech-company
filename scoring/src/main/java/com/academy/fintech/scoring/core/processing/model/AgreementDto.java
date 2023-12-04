package com.academy.fintech.scoring.core.processing.model;

import java.time.LocalDate;
import java.util.UUID;

public record AgreementDto(
        UUID agreementId,
        LocalDate nextPaymentDate
) {
}
