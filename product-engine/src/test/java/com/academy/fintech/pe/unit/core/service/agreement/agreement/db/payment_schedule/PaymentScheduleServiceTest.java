package com.academy.fintech.pe.unit.core.service.agreement.agreement.db.payment_schedule;

import com.academy.fintech.pe.core.service.agreement.db.agreement.Agreement;
import com.academy.fintech.pe.core.service.agreement.db.payment_schedule.PaymentSchedule;
import com.academy.fintech.pe.core.service.agreement.db.payment_schedule.PaymentScheduleRepository;
import com.academy.fintech.pe.core.service.agreement.db.payment_schedule.PaymentScheduleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentScheduleServiceTest {

    @Mock
    private PaymentScheduleRepository repository;

    @InjectMocks
    private PaymentScheduleService service;

    @Test
    void givenAgreementId_whenCreateNewSchedule_thenReturnScheduleWithNewVersion() {
        int oldScheduleVersion = 2;
        Agreement agreement = new Agreement();
        agreement.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        when(repository.countPaymentScheduleByAgreement(agreement)).thenReturn(oldScheduleVersion);
        when(repository.save(any())).thenAnswer(method -> method.getArguments()[0]);

        PaymentSchedule actualSchedule = service.createNewSchedule(agreement);

        assertThat(actualSchedule.getAgreement()).isEqualTo(agreement);
        assertThat(actualSchedule.getVersion()).isEqualTo(oldScheduleVersion + 1);
    }
}