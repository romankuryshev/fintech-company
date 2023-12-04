package com.academy.fintech.origination.core.scoring.client.grpc;

import com.academy.fintech.scoring.application_processing.ApplicationProcessingServiceGrpc;
import com.academy.fintech.scoring.application_processing.ApplicationProcessingServiceGrpc.ApplicationProcessingServiceBlockingStub;
import com.academy.fintech.scoring.application_processing.ProcessApplicationRequest;
import com.academy.fintech.scoring.application_processing.ProcessApplicationResponse;
import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ScoringGrpcClient {

    private final ApplicationProcessingServiceBlockingStub stub;

    public ScoringGrpcClient(ScoringGrpcClientProperty property) {
        Channel channel = ManagedChannelBuilder.forAddress(property.host(), property.port()).usePlaintext().build();
        this.stub = ApplicationProcessingServiceGrpc.newBlockingStub(channel);
    }

    public ProcessApplicationResponse processApplication(ProcessApplicationRequest request) {
        return stub.processApplication(request);
    }
}
