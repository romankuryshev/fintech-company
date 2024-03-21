package com.academy.fintech.pg.core.payments.db;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository repository;

    public Payment save(Payment payment) {
        return repository.save(payment);
    }
}
