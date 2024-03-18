package com.academy.fintech.mp.public_interface;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateDisbursementRequest(
        UUID agreementId,
        BigDecimal amount
) {
}
