package com.academy.fintech.pe.grpc.service.agreement.payment_schedule.dto;

import java.time.LocalDate;
import java.util.UUID;

public record PaymentScheduleRequestDto(
        UUID agreementId,
        LocalDate disbursementDate
) {}
