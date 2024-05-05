package com.academy.fintech.pg.public_interface;

import java.math.BigDecimal;
import java.util.UUID;

public record DisbursementDto(UUID agreementId, BigDecimal amount)  {
}
