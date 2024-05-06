package com.academy.fintech.pe.core.service.dwh.dto;

public record AgreementMessage (
    String id,
    String productCode,
    String clientId,
    String interest,
    int termInMonths,
    String principalAmount,
    String originationAmount,
    String status,
    String disbursementDate,
    String nextPaymentDate,
    String eventDatetime
){}
