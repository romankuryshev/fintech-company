package com.academy.fintech.mp.core.db;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DisbursementService {

    private final DisbursementRepository repository;

    public List<Disbursement> getDisbursementsWithPassedDateTime() {
        return repository.findAllByStatusAndDateTimeBefore(DisbursementStatus.AWAITS, LocalDateTime.now());
    }

    public Disbursement getDisbursementBy(UUID agreementId) {
        return repository.findByAgreementId(agreementId);
    }

    public Disbursement save(Disbursement disbursement) {
        return repository.save(disbursement);
    }

    public List<Disbursement> saveAll(Collection<Disbursement> collection) {
        return repository.saveAll(collection);
    }
}
