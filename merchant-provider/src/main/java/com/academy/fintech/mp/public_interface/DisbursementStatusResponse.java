package com.academy.fintech.mp.public_interface;

import com.academy.fintech.mp.core.db.DisbursementStatus;
import lombok.Builder;

import java.util.UUID;

@Builder
public record DisbursementStatusResponse(
        UUID agreementId,
        DisbursementStatus status
) {
}
