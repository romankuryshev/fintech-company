package com.academy.fintech.pe.core.service.agreement.db.payment_schedule;

import com.academy.fintech.pe.core.service.agreement.db.agreement.Agreement;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class PaymentScheduleService {

    private final PaymentScheduleRepository paymentScheduleRepository;

    public PaymentSchedule createNewSchedule(Agreement agreement) {
        int currentVersion = paymentScheduleRepository.countPaymentScheduleByAgreement(agreement);
        return paymentScheduleRepository.save(new PaymentSchedule(agreement, currentVersion + 1));
    }

    public PaymentSchedule getActualSchedule(Agreement agreement) {
        return paymentScheduleRepository.findFirstByAgreementOrderByVersionAsc(agreement);
    }

    public PaymentSchedule save(PaymentSchedule schedule) {
        return paymentScheduleRepository.save(schedule);
    }
}
