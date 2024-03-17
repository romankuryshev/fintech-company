package com.academy.fintech.pg.core.disbursement;

import com.academy.fintech.pg.core.disbursement.db.Disbursement;
import com.academy.fintech.pg.core.disbursement.db.DisbursementService;
import com.academy.fintech.pg.core.disbursement.db.DisbursementStatus;
import com.academy.fintech.pg.core.disbursement.provider.merchant.MerchantProviderService;
import com.academy.fintech.pg.public_interface.DisbursementDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DisbursementExecutionService {

    private final DisbursementService disbursementService;

    private final MerchantProviderService merchantProviderService;

    public void executeDisbursement(DisbursementDto disbursementDto) {
        Disbursement disbursement = createEntityFromDto(disbursementDto);
        disbursementService.save(disbursement);

//        merchantProviderService.sendDisbursement(disbursement);
    }

    private Disbursement createEntityFromDto(DisbursementDto disbursementDto) {
        return Disbursement.builder()
                .agreementId(disbursementDto.agreementId())
                .amount(disbursementDto.amount())
                .status(DisbursementStatus.AWAITS)
                .dateTime(LocalDateTime.now())
                .build();
    }
}
