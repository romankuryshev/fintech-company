package com.academy.fintech.origination.core.pg.client;

import com.academy.fintech.origination.core.pg.client.grpc.PaymentGateGrpcClient;
import com.academy.fintech.origination.core.service.application.db.application.Application;
import com.academy.fintech.pg.payment_service.DisbursementRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PaymentGateClientService {

    private final PaymentServiceMapper mapper;

    private final PaymentGateGrpcClient paymentGateGrpcClient;

    public void executePayment(Application application) {
        DisbursementRequest request = mapper.toPaymentRequest(application);
        paymentGateGrpcClient.executePayment(request);
    }
}
