package com.academy.fintech.pe.core.service.agreement.db.loan_payment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoanPaymentService {

    private final LoanPaymentRepository loanPaymentRepository;

    @Autowired
    public LoanPaymentService(LoanPaymentRepository loanPaymentRepository) {
        this.loanPaymentRepository = loanPaymentRepository;
    }

    public List<LoanPayment> saveAll(List<LoanPayment> payments) {
        return loanPaymentRepository.saveAll(payments);
    }
}
