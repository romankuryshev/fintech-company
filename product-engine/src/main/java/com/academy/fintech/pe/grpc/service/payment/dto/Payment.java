package com.academy.fintech.pe.grpc.service.payment.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record Payment(
        int id,
        UUID agreementId,
        BigDecimal amount

) {
}
