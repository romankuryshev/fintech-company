package com.academy.fintech.pe.core.service.agreement.db.agreement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface AgreementRepository extends JpaRepository<Agreement, UUID> {

    List<Agreement> findAllByClientId(UUID clientId);

    List<Agreement> findAllByNextPaymentDateAfter(LocalDate nextPaymentDate);
}
