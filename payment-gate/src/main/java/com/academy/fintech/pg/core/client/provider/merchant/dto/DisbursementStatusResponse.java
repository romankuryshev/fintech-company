package com.academy.fintech.pg.core.client.provider.merchant.dto;

import com.academy.fintech.pg.core.disbursement.db.DisbursementStatus;

public record DisbursementStatusResponse(
        DisbursementStatus status
) {
}
