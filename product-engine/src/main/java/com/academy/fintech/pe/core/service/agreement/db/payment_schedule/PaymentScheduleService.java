package com.academy.fintech.pe.core.service.agreement.db.payment_schedule;

import com.academy.fintech.pe.core.service.agreement.db.agreement.Agreement;
import org.springframework.stereotype.Service;


@Service
public class PaymentScheduleService {

    private final PaymentScheduleRepository paymentScheduleRepository;

    public PaymentScheduleService(PaymentScheduleRepository paymentScheduleRepository) {
        this.paymentScheduleRepository = paymentScheduleRepository;
    }

    public PaymentSchedule createNewSchedule(Agreement agreement) {
        int currentVersion = paymentScheduleRepository.countPaymentScheduleByAgreement(agreement);
        return paymentScheduleRepository.save(new PaymentSchedule(agreement, currentVersion + 1));
    }

    public PaymentSchedule save(PaymentSchedule schedule) {
        return paymentScheduleRepository.save(schedule);
    }
}
