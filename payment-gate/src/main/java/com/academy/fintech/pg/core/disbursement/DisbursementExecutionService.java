package com.academy.fintech.pg.core.disbursement;

import com.academy.fintech.pg.core.client.provider.merchant.MerchantProviderService;
import com.academy.fintech.pg.core.disbursement.db.Disbursement;
import com.academy.fintech.pg.core.disbursement.db.DisbursementService;
import com.academy.fintech.pg.core.disbursement.db.DisbursementStatus;
import com.academy.fintech.pg.public_interface.DisbursementDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DisbursementExecutionService {

    private static final int FIRST_CHECK_INTERVAL = 1;

    private final DisbursementService disbursementDbService;

    private final MerchantProviderService merchantProviderClientService;

    public void executeDisbursement(DisbursementDto disbursementDto) {
        Disbursement disbursement = createEntityFromDto(disbursementDto);
        disbursementDbService.save(disbursement);

        merchantProviderClientService.sendDisbursement(disbursement);
    }

    private Disbursement createEntityFromDto(DisbursementDto disbursementDto) {
        LocalDateTime creationDateTime = LocalDateTime.now();
        return Disbursement.builder()
                .agreementId(disbursementDto.agreementId())
                .amount(disbursementDto.amount())
                .status(DisbursementStatus.AWAITS)
                .dateTime(creationDateTime)
                .checkCount(0)
                .nextCheckDate(creationDateTime.plusMinutes(FIRST_CHECK_INTERVAL))
                .build();
    }
}
