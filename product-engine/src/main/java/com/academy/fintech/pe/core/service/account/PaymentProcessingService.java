package com.academy.fintech.pe.core.service.account;

import com.academy.fintech.pe.core.service.account.db.Account;
import com.academy.fintech.pe.core.service.account.db.AccountService;
import com.academy.fintech.pe.grpc.service.payment.dto.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class PaymentProcessingService {

    private final AccountService accountService;

    public void processPayment(Payment payment) {
        Account account = accountService.getByAgreementId(payment.agreementId());
        BigDecimal currentDuty = account.getDuty();
        if (currentDuty.compareTo(BigDecimal.ZERO) > 0 && currentDuty.compareTo(payment.amount()) >= 0) {
            account.setDuty(currentDuty.subtract(payment.amount()));
        }
        else if (currentDuty.compareTo(BigDecimal.ZERO) > 0 && currentDuty.compareTo(payment.amount()) < 0) {
            account.setDuty(BigDecimal.ZERO);
            account.setBalance(payment.amount().subtract(currentDuty));
        } else {
            account.setBalance(account.getBalance().add(payment.amount()));
        }

        accountService.save(account);
    }
}
