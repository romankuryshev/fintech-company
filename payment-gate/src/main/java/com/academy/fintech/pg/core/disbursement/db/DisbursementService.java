package com.academy.fintech.pg.core.disbursement.db;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DisbursementService {

    private final DisbursementRepository disbursementRepository;

    public Disbursement save(Disbursement disbursement) {
        return disbursementRepository.save(disbursement);
    }
}
