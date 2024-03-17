package com.academy.fintech.origination.core.pg.client.grpc;

import com.academy.fintech.pg.payment_service.DisbursementRequest;
import com.academy.fintech.pg.payment_service.DisbursementServiceGrpc;
import com.academy.fintech.pg.payment_service.Empty;
import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PaymentGateGrpcClient {

    private final DisbursementServiceGrpc.DisbursementServiceBlockingStub stub;

    public PaymentGateGrpcClient(PaymentGateGrpcProperty property) {
        Channel channel = ManagedChannelBuilder.forAddress(property.host(), property.port())
                .usePlaintext()
                .build();
        this.stub = DisbursementServiceGrpc.newBlockingStub(channel);
    }

    public void executePayment(DisbursementRequest request) {
        try {
            stub.executeDisbursement(request);
        } catch (StatusRuntimeException e) {
            log.error("Got error from Payment Gate with request: {}", request, e);
            throw e;
        }
    }

}
