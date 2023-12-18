package com.academy.fintech.api.core.origination.client.grpc;

import com.academy.fintech.application.ApplicationServiceGrpc;
import com.academy.fintech.application.ApplicationServiceGrpc.ApplicationServiceBlockingStub;
import com.academy.fintech.application.CreateRequest;
import com.academy.fintech.application.CreateResponse;
import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OriginationGrpcClient {

    private final ApplicationServiceBlockingStub stub;

    public OriginationGrpcClient(OriginationGrpcClientProperty property) {
        Channel channel = ManagedChannelBuilder.forAddress(property.host(), property.port()).usePlaintext().build();
        this.stub = ApplicationServiceGrpc.newBlockingStub(channel);
    }

    public CreateResponse createApplication(CreateRequest applicationRequest) {
        try {
            return stub.create(applicationRequest);
        } catch (StatusRuntimeException e) {
            log.error("Got error from Origination by request: {}", applicationRequest, e);
            throw e;
        }
    }

}
