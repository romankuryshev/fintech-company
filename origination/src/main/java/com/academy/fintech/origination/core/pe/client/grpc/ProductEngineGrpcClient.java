package com.academy.fintech.origination.core.pe.client.grpc;

import com.academy.fintech.pe.grpc.service.agreement.agreement.AgreementCreationServiceGrpc;
import com.academy.fintech.pe.grpc.service.agreement.agreement.AgreementCreationServiceGrpc.AgreementCreationServiceBlockingStub;
import com.academy.fintech.pe.grpc.service.agreement.agreement.AgreementRequest;
import com.academy.fintech.pe.grpc.service.agreement.agreement.AgreementResponse;
import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ProductEngineGrpcClient {
    private final AgreementCreationServiceBlockingStub stub;

    public ProductEngineGrpcClient(ProductEngineGrpcClientProperty property) {
        Channel channel = ManagedChannelBuilder.forAddress(property.host(), property.port())
                .usePlaintext()
                .build();
        this.stub = AgreementCreationServiceGrpc.newBlockingStub(channel);
    }

    public AgreementResponse createAgreement(AgreementRequest request) throws StatusRuntimeException {
        try {
            return stub.create(request);
        } catch (StatusRuntimeException e) {
            log.error("Got error from Product Engine by request: {}", request, e);
            throw e;
        }
    }
}
