package com.academy.fintech.pe.core.service.account;

import com.academy.fintech.pe.core.service.agreement.db.agreement.Agreement;
import com.academy.fintech.pe.core.service.agreement.db.loan_payment.LoanPayment;
import com.academy.fintech.pe.core.service.agreement.db.loan_payment.LoanPaymentService;
import com.academy.fintech.pe.core.service.agreement.db.loan_payment.LoanPaymentStatus;
import com.academy.fintech.pe.core.service.agreement.db.payment_schedule.PaymentSchedule;
import com.academy.fintech.pe.core.service.agreement.db.payment_schedule.PaymentScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class TransactionTemplate {
    private final AccountService accountService;
    private final LoanPaymentService loanPaymentService;
    private final PaymentScheduleService paymentScheduleService;

    @Transactional
    public void update(Agreement agreement) {
        PaymentSchedule schedule = paymentScheduleService.getActualSchedule(agreement);
        LoanPayment payment = loanPaymentService.getByScheduleAndDate(schedule, LocalDate.now());
        Account account = accountService.getByAgreementId(agreement.getId());

        if (accountService.reduceBalance(account, payment.getPeriodPayment())) {
            payment.setStatus(LoanPaymentStatus.PAID);
        } else {
            payment.setStatus(LoanPaymentStatus.OVERDUE);
        }

        LoanPayment nextPayment = loanPaymentService.getByScheduleIdAndPeriodNumber(schedule, payment.getPeriodNumber());
        agreement.setNextPaymentDate(nextPayment.getPaymentDate());

        accountService.save(account);
        loanPaymentService.save(payment);
    }
}
