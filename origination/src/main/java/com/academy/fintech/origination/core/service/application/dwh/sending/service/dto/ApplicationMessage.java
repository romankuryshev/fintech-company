package com.academy.fintech.origination.core.service.application.dwh.sending.service.dto;

public record ApplicationMessage(
        String clientId,
        double requestDisbursementAmount,
        String status,
        String agreementId
) {
}
