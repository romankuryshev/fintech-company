package com.academy.fintech.pe.core.service.account.db;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    public Account getByAgreementId(UUID agreementId) {
        return accountRepository.findByAgreementId(agreementId);
    }

    public Account save(Account account) {
        return accountRepository.save(account);
    }

    public Account create(UUID agreementId) {
        Account account = Account.builder()
                .agreementId(agreementId)
                .balance(BigDecimal.ZERO)
                .duty(BigDecimal.ZERO)
                .build();
        return accountRepository.save(account);
    }

    public boolean reduceBalance(Account account, BigDecimal amount) {
        BigDecimal balance = account.getBalance();
        boolean result;
        if (account.getBalance().compareTo(amount) >= 0) {
            account.setBalance(balance.subtract(amount));
            result = true;
        }
        else {
            account.setBalance(BigDecimal.ZERO);
            account.setDuty(amount.subtract(balance));
            result = false;
        }
        save(account);
        return result;
    }
}
