package com.academy.fintech.scoring.core.pe.client.grpc;

import com.academy.fintech.scoring.application_processing.AdvancedPaymentRequest;
import com.academy.fintech.scoring.application_processing.AdvancedPaymentResponse;
import com.academy.fintech.scoring.application_processing.ClientStatisticRequest;
import com.academy.fintech.scoring.application_processing.ClientStatisticResponse;
import com.academy.fintech.scoring.application_processing.ProductRequest;
import com.academy.fintech.scoring.application_processing.ProductResponse;
import com.academy.fintech.scoring.application_processing.ScoringDataServiceGrpc;
import com.academy.fintech.scoring.application_processing.ScoringDataServiceGrpc.ScoringDataServiceBlockingStub;
import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ProductEngineGrpcClient {

    private final ScoringDataServiceBlockingStub stub;

    public ProductEngineGrpcClient(ProductEngineGrpcClientProperty property) {
        Channel channel = ManagedChannelBuilder.forAddress(property.host(), property.port())
                .usePlaintext()
                .build();
        this.stub = ScoringDataServiceGrpc.newBlockingStub(channel);
    }

    public ProductResponse getProductInfo(ProductRequest request) {
        try {
            return stub.getProductInfo(request);
        } catch (StatusRuntimeException e) {
            log.error("Got error from Product Engine by request: {}", request, e);
            throw e;
        }
    }

    public ClientStatisticResponse getClientStatistic(ClientStatisticRequest request) {
        try {
            return stub.getClientInfo(request);
        } catch (StatusRuntimeException e) {
            log.error("Got error from Product Engine by request: {}", request, e);
            throw e;
        }
    }

    public AdvancedPaymentResponse getPaymentAmount(AdvancedPaymentRequest request) {
        try {
            return stub.getPaymentAmount(request);
        } catch (StatusRuntimeException e) {
            log.error("Got error from Product Engine by request: {}", request, e);
            throw e;
        }
    }
}
