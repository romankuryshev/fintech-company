package com.academy.fintech.pg.public_interface;

import java.math.BigDecimal;
import java.util.UUID;

public record PaymentDto(
        UUID agreementId,
        BigDecimal amount
) {
}
