package com.academy.fintech.dwh.core.application;

public record ApplicationMessage(
        String clientId,
        double requestDisbursementAmount,
        String status,
        String agreementId,
        String eventDatetime
) {
}
