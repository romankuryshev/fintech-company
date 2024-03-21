package com.academy.fintech.pg.core.disbursement;

import com.academy.fintech.pg.core.client.origination.OriginationClientService;
import com.academy.fintech.pg.core.client.pe.ProductEngineClientService;
import com.academy.fintech.pg.core.client.provider.merchant.MerchantProviderService;
import com.academy.fintech.pg.core.disbursement.db.Disbursement;
import com.academy.fintech.pg.core.disbursement.db.DisbursementService;
import com.academy.fintech.pg.core.disbursement.db.DisbursementStatus;
import com.academy.fintech.pg.core.payments.db.PaymentService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Component
public class ScheduledProviderPoller {

    private static final int FIXED_RATE = 60000;

    private final DisbursementService disbursementService;
    private final MerchantProviderService merchantProviderService;
    private final OriginationClientService originationClientService;
    private Map<Integer, Function<LocalDateTime, LocalDateTime>> checkTimings;

    public ScheduledProviderPoller(DisbursementService disbursementService, MerchantProviderService merchantProviderService, OriginationClientService originationClientService) {
        this.merchantProviderService = merchantProviderService;
        this.disbursementService = disbursementService;
        this.originationClientService = originationClientService;
        checkTimings = new HashMap<>();
        checkTimings.put(0, dateTime -> (dateTime.plusMinutes(5)));
        checkTimings.put(1, dateTime -> (dateTime.plusMinutes(10)));
        checkTimings.put(2, dateTime -> (dateTime.plusMinutes(30)));
        checkTimings.put(3, dateTime -> (dateTime.plusDays(1)));
        checkTimings.put(4, dateTime -> (dateTime.plusDays(3)));
        checkTimings.put(5, dateTime -> (dateTime.plusDays(5)));
        checkTimings.put(6, dateTime -> (dateTime.plusDays(5)));
    }

    @Scheduled(fixedRate = FIXED_RATE)
    public void checkDisbursements() {
        List<Disbursement> disbursementList = disbursementService.getDisbursementForCheck();

        disbursementList.forEach(disbursement -> {
            DisbursementStatus status = merchantProviderService.checkStatus(disbursement);
            if (status == DisbursementStatus.COMPLETED) {
                disbursement.setStatus(status);
                originationClientService.confirmDisbursement(disbursement);
            }
            else if (disbursement.getCheckCount() < 6){
                int count = disbursement.incrementCheckCount();
                disbursement.setNextCheckDate(checkTimings.get(count).apply(disbursement.getDateTime()));
            }
            else {
                disbursement.setStatus(DisbursementStatus.ERROR);
            }

            disbursementService.save(disbursement);
        });
    }
}
