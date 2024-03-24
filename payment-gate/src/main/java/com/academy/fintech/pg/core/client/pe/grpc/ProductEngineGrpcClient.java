package com.academy.fintech.pg.core.client.pe.grpc;

import com.academy.fintech.pg.payment_processing_service.Empty;
import com.academy.fintech.pg.payment_processing_service.PaymentProcessingServiceGrpc;
import com.academy.fintech.pg.payment_processing_service.ProcessPaymentRequest;
import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ProductEngineGrpcClient {

    private final PaymentProcessingServiceGrpc.PaymentProcessingServiceBlockingStub stub;

    public ProductEngineGrpcClient(ProductEngineGrpcClientProperty property) {
        Channel channel = ManagedChannelBuilder.forAddress(property.host(), property.port())
                .usePlaintext()
                .build();
        this.stub = PaymentProcessingServiceGrpc.newBlockingStub(channel);
    }

    public void createPayment(ProcessPaymentRequest request) {
        try {
            Empty empty = stub.process(request);
        }catch (StatusRuntimeException e) {
            log.error("Got error by request {}", request);
            throw e;
        }
    }
}
