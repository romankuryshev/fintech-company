package com.academy.fintech.pg.rest.payments;

import com.academy.fintech.pg.core.payments.PaymentProcessingService;
import com.academy.fintech.pg.core.payments.db.Payment;
import com.academy.fintech.pg.public_interface.PaymentDto;
import com.academy.fintech.pg.public_interface.mapper.PaymentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PaymentsController {

    private final PaymentProcessingService paymentProcessingService;

    private final PaymentMapper paymentMapper;

    @PostMapping("/payments/create")
    public CreatePaymentResponse createPayment(PaymentDto paymentDto) {
        Payment payment = paymentProcessingService.processPayment(paymentDto);
        return paymentMapper.toCreatePaymentResponse(payment);
    }
}
