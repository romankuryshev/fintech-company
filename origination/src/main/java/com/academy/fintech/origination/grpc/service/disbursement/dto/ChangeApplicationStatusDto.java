package com.academy.fintech.origination.grpc.service.disbursement.dto;

import com.academy.fintech.origination.core.service.application.db.application.ApplicationStatus;
import lombok.Builder;

import java.time.LocalDate;
import java.util.UUID;

@Builder
public record ChangeApplicationStatusDto(
        UUID agreementId,
        ApplicationStatus status,
        LocalDate disbursementDate
) {
}
