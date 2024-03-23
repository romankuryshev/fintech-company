package com.academy.fintech.mp.core.payment_mock;

import com.academy.fintech.mp.core.db.DisbursementService;
import com.academy.fintech.mp.core.db.Disbursement;
import com.academy.fintech.mp.core.db.DisbursementStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ScheduledUpdater {

    private static final int SCHEDULED_RATE = 1000;

    private final DisbursementService disbursementService;

    @Scheduled(fixedRate = SCHEDULED_RATE)
    public void updateDisbursements() {
        List<Disbursement> passedDisbursements = disbursementService.getDisbursementsWithPassedDateTime();
        passedDisbursements.forEach(disbursement -> disbursement.setStatus(DisbursementStatus.COMPLETED));

        disbursementService.saveAll(passedDisbursements);
    }
}
