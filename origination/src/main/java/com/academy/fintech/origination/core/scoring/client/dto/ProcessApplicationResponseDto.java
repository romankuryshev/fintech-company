package com.academy.fintech.origination.core.scoring.client.dto;

import com.academy.fintech.origination.core.service.application.db.application.ApplicationStatus;

public record ProcessApplicationResponseDto(
        String applicationId,
        ApplicationStatus status
) {
}
