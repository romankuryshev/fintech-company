package com.academy.fintech.mp.core.db;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface DisbursementRepository extends JpaRepository<Disbursement, Long> {

    List<Disbursement> findAllByStatusAndDateTimeBefore(DisbursementStatus status, LocalDateTime dateTime);
    Disbursement findByAgreementId(UUID agreementId);
}
