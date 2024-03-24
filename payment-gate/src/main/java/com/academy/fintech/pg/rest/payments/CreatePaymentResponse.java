package com.academy.fintech.pg.rest.payments;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record CreatePaymentResponse(
        int id,
        UUID agreementId,
        LocalDateTime dateTime,
        BigDecimal amount
) {
}
