package com.academy.fintech.pg.core.payments;

import com.academy.fintech.pg.core.client.pe.ProductEngineClientService;
import com.academy.fintech.pg.core.payments.db.Payment;
import com.academy.fintech.pg.core.payments.db.PaymentService;
import com.academy.fintech.pg.public_interface.PaymentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class PaymentProcessingService {

    private final PaymentService paymentService;

    public final ProductEngineClientService productEngineClientService;


    public Payment processPayment(PaymentDto paymentDto) {
        Payment payment = createEntityFromDto(paymentDto);
        payment = paymentService.save(payment);
        productEngineClientService.sendPayment(payment);
        return payment;
    }

    private Payment createEntityFromDto(PaymentDto paymentDto) {
        return Payment.builder()
                .agreementId(paymentDto.agreementId())
                .date(LocalDate.now())
                .amount(paymentDto.amount())
                .build();
    }
}
