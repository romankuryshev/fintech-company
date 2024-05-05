package com.academy.fintech.pg.core.disbursement.db;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class DisbursementService {

    private final DisbursementRepository disbursementRepository;

    public List<Disbursement> getDisbursementForCheck() {
        return disbursementRepository.findAllByStatusAndNextCheckDateBefore(DisbursementStatus.AWAITS, LocalDateTime.now());
    }

    public Disbursement save(Disbursement disbursement) {
        return disbursementRepository.save(disbursement);
    }
}
