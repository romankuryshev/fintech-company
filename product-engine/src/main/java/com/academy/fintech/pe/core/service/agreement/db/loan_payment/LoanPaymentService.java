package com.academy.fintech.pe.core.service.agreement.db.loan_payment;

import com.academy.fintech.pe.core.service.agreement.db.payment_schedule.PaymentSchedule;
import com.academy.fintech.pe.grpc.service.payment.dto.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanPaymentService {

    private final LoanPaymentRepository loanPaymentRepository;

    public LoanPayment getByScheduleIdAndPeriodNumber(PaymentSchedule schedule, int periodNumber) {
        return loanPaymentRepository.findByPaymentScheduleAndPeriodNumber(schedule, periodNumber);
    }

    public LoanPayment getByScheduleAndDate(PaymentSchedule schedule, LocalDate date) {
        return loanPaymentRepository.findByPaymentScheduleAndPaymentDate(schedule, date);
    }

    public LoanPayment save(LoanPayment loanPayment) {
        return loanPaymentRepository.save(loanPayment);
    }
    public List<LoanPayment> saveAll(List<LoanPayment> payments) {
        return loanPaymentRepository.saveAll(payments);
    }
}
