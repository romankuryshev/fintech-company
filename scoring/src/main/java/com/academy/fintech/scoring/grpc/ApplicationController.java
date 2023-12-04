package com.academy.fintech.scoring.grpc;

import com.academy.fintech.scoring.application_processing.ApplicationProcessingServiceGrpc;
import com.academy.fintech.scoring.application_processing.ProcessApplicationRequest;
import com.academy.fintech.scoring.application_processing.ProcessApplicationResponse;
import com.academy.fintech.scoring.core.processing.model.ProcessingResult;
import com.academy.fintech.scoring.core.processing.ScoringService;
import com.academy.fintech.scoring.public_interface.processing.dto.ProcessApplicationRequestDto;
import com.academy.fintech.scoring.grpc.mapper.ProcessMapper;
import io.grpc.stub.StreamObserver;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@Slf4j
@AllArgsConstructor
public class ApplicationController extends ApplicationProcessingServiceGrpc.ApplicationProcessingServiceImplBase {

    private final ScoringService scoringService;

    private final ProcessMapper mapper;

    @Override
    public void processApplication(ProcessApplicationRequest request, StreamObserver<ProcessApplicationResponse> responseObserver) {
        log.info("request with application id " + request.getApplicationId());
        ProcessApplicationRequestDto requestDto = mapper.toDto(request);
        ProcessingResult result = scoringService.process(requestDto);

        log.info("response with status" + mapper.toResponse(result));
        responseObserver.onNext(mapper.toResponse(result));
        responseObserver.onCompleted();
    }
}
