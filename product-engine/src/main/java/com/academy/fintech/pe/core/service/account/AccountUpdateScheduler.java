package com.academy.fintech.pe.core.service.account;

import com.academy.fintech.pe.core.service.account.db.Account;
import com.academy.fintech.pe.core.service.account.db.AccountService;
import com.academy.fintech.pe.core.service.agreement.db.agreement.Agreement;
import com.academy.fintech.pe.core.service.agreement.db.agreement.AgreementService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AccountUpdateScheduler {

    private final AgreementService agreementService;
    private final TransactionUpdater transactionUpdater;

    @Scheduled(cron = "0 0 2 * * *", zone = "Europe/Moscow")
    public void updateAccounts() {
        List<Agreement> overdueAgreements = agreementService.getAllNewOverdueAgreements();
        overdueAgreements.forEach(transactionUpdater::update);
    }
}
