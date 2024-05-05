package com.academy.fintech.pg.core.disbursement.db;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface DisbursementRepository extends JpaRepository<Disbursement, Long> {

    List<Disbursement> findAllByStatusAndNextCheckDateBefore(DisbursementStatus status, LocalDateTime dateTime);
}
