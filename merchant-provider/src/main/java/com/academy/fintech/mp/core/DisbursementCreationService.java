package com.academy.fintech.mp.core;

import com.academy.fintech.mp.core.db.DisbursementService;
import com.academy.fintech.mp.core.db.DisbursementStatus;
import com.academy.fintech.mp.public_interface.CreateDisbursementRequest;
import com.academy.fintech.mp.core.db.Disbursement;
import com.academy.fintech.mp.public_interface.DisbursementStatusResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class DisbursementCreationService {

    private static final int MIN_DELAY_IN_MINUTES = 1;
    private static final int MAX_DELAY_IN_MINUTES = 10;  // for 7 days: 60 * 24 * 7 = 10080

    private final DisbursementService disbursementService;

    public void createDisbursement(CreateDisbursementRequest request) {
        Disbursement disbursement = createEntity(request);
        disbursementService.save(disbursement);
    }

    public DisbursementStatusResponse getStatus(UUID agreementId) {
        Disbursement disbursement = disbursementService.getDisbursementBy(agreementId);
        return DisbursementStatusResponse.builder()
                .agreementId(disbursement.getAgreementId())
                .status(disbursement.getStatus())
                .build();
    }

    private Disbursement createEntity(CreateDisbursementRequest request) {
        int delay = (int) (Math.random() * MAX_DELAY_IN_MINUTES) + MIN_DELAY_IN_MINUTES;
        return Disbursement.builder()
                .agreementId(request.agreementId())
                .amount(request.amount())
                .dateTime(LocalDateTime.now().plusMinutes(delay))
                .status(DisbursementStatus.AWAITS)
                .build();
    }
}
