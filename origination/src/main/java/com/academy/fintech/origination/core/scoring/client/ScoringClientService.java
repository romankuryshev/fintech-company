package com.academy.fintech.origination.core.scoring.client;

import com.academy.fintech.origination.core.scoring.client.grpc.ScoringGrpcClient;
import com.academy.fintech.origination.core.service.application.db.application.Application;
import com.academy.fintech.origination.core.service.application.db.application.ApplicationStatus;
import com.academy.fintech.scoring.application_processing.ProcessApplicationRequest;
import com.academy.fintech.scoring.application_processing.ProcessApplicationResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class ScoringClientService {

    private final ScoringGrpcClient scoringGrpcClient;

    private final ProcessingMapper processingMapper;

    public ApplicationStatus processApplication(Application application) {
        ProcessApplicationRequest request = processingMapper.toRequest(application);
        ProcessApplicationResponse response = scoringGrpcClient.processApplication(request);

        return processingMapper.toApplicationStatus(response.getStatus());
    }
}
