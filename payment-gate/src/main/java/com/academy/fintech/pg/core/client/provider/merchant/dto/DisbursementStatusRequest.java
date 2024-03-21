package com.academy.fintech.pg.core.client.provider.merchant.dto;

import java.util.UUID;

public record DisbursementStatusRequest(
        UUID disbursementId
) {
}
