package com.academy.fintech.pg.core.client.origination.grpc;

import com.academy.fintech.pg.payment_service.ChangeStatusRequest;
import com.academy.fintech.pg.payment_service.DisbursementConfirmServiceGrpc;
import com.academy.fintech.pg.payment_service.Empty;
import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class OriginationGrpcClient {

    private final DisbursementConfirmServiceGrpc.DisbursementConfirmServiceBlockingStub stub;


    public OriginationGrpcClient(OriginationGrpcClientProperties properties) {
        Channel channel = ManagedChannelBuilder.forAddress(properties.host(), properties.port())
                .usePlaintext()
                .build();
        this.stub = DisbursementConfirmServiceGrpc.newBlockingStub(channel);
    }

    public void changeStatus(ChangeStatusRequest request) {
        try {
            Empty empty = stub.changeStatus(request);
        } catch (StatusRuntimeException e) {
            log.error("Got error with request: {}", request);
            throw e;
        }
    }
}
