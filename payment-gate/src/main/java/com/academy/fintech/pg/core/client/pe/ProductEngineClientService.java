package com.academy.fintech.pg.core.client.pe;

import com.academy.fintech.pg.core.client.pe.rest.ProductEngineRestClient;
import com.academy.fintech.pg.core.payments.db.Payment;
import com.academy.fintech.pg.public_interface.mapper.PaymentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductEngineClientService {

    private final ProductEngineRestClient productEngineRestClient;

    private final PaymentMapper paymentMapper;

    public void sendPayment(Payment payment) {
        productEngineRestClient.createPayment(paymentMapper.toDto(payment));
    }
}
