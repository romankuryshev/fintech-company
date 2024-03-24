package com.academy.fintech.pe.core.service.agreement.db.loan_payment;

import com.academy.fintech.pe.core.service.agreement.db.payment_schedule.PaymentSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface LoanPaymentRepository extends JpaRepository<LoanPayment, Integer> {
    LoanPayment findByPaymentScheduleAndPeriodNumber(PaymentSchedule schedule, int periodNumber);
    LoanPayment findByPaymentScheduleAndPaymentDate(PaymentSchedule schedule, LocalDate paymentDate);
}
