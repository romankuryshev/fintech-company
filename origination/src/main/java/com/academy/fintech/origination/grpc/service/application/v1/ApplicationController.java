package com.academy.fintech.origination.grpc.service.application.v1;

import com.academy.fintech.application.*;
import com.academy.fintech.origination.core.service.application.ApplicationRequestService;
import com.academy.fintech.origination.core.service.application.db.application.Application;
import com.academy.fintech.origination.grpc.service.application.v1.dto.CancelRequestDto;
import com.academy.fintech.origination.grpc.service.application.v1.dto.ClientDto;
import com.academy.fintech.origination.grpc.service.application.v1.dto.CreateRequestDto;
import com.academy.fintech.origination.grpc.service.application.v1.mapper.ApplicationMapper;
import com.academy.fintech.origination.grpc.service.application.v1.mapper.ClientMapper;
import io.grpc.stub.StreamObserver;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

@Slf4j
@GrpcService
@AllArgsConstructor
public class ApplicationController extends ApplicationServiceGrpc.ApplicationServiceImplBase {

    private final ClientMapper clientMapper;

    private final ApplicationMapper applicationMapper;

    private final ApplicationRequestService applicationRequestService;

    @Override
    public void create(CreateRequest request, StreamObserver<CreateResponse> responseObserver) {
        log.info("Got request: {}", request);
        ClientDto clientDto = clientMapper.toDto(request);
        CreateRequestDto createRequestDto = applicationMapper.toDto(request);

        Application application = applicationRequestService.createApplication(clientDto, createRequestDto);
        CreateResponse response = applicationMapper.toResponse(application);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void remove(CancelRequest request, StreamObserver<CancelResponse> responseObserver) {
        CancelRequestDto dto = applicationMapper.toDto(request);
        applicationRequestService.cancelApplication(dto);
        responseObserver.onNext(CancelResponse.newBuilder()
                .setSuccess(true)
                .build());
        responseObserver.onCompleted();
    }
}
