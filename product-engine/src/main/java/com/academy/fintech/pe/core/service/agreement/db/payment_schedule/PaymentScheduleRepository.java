package com.academy.fintech.pe.core.service.agreement.db.payment_schedule;

import com.academy.fintech.pe.core.service.agreement.db.agreement.Agreement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentScheduleRepository extends JpaRepository<PaymentSchedule, Long> {
    Integer countPaymentScheduleByAgreement(Agreement agreement);
    Optional<PaymentSchedule> findByAgreement_IdAndVersion(UUID AgreementId, int version);
}
