package com.academy.fintech.scoring.core.pe.client.grpc;

import com.academy.fintech.scoring.application_processing.AdvancedPaymentRequest;
import com.academy.fintech.scoring.application_processing.AdvancedPaymentResponse;
import com.academy.fintech.scoring.application_processing.ClientAgreementsRequest;
import com.academy.fintech.scoring.application_processing.ClientAgreementsResponse;
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

    public ProductResponse getProduct(ProductRequest request) {
        try {
            return stub.getProduct(request);
        } catch (StatusRuntimeException e) {
            log.error("Got error from Product Engine by request: {}", request, e);
            throw e;
        }
    }

    public ClientAgreementsResponse getClientAgreements(ClientAgreementsRequest request) {
        try {
            return stub.getClientAgreements(request);
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
