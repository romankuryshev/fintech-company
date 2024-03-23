package com.academy.fintech.origination.grpc.service.disbursement;

import com.academy.fintech.origination.core.service.application.ApplicationRequestService;
import com.academy.fintech.pg.payment_service.ChangeStatusRequest;
import com.academy.fintech.pg.payment_service.DisbursementConfirmServiceGrpc;
import com.academy.fintech.pg.payment_service.Empty;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class DisbursementConfirmationController extends DisbursementConfirmServiceGrpc.DisbursementConfirmServiceImplBase {

    private final ApplicationRequestService applicationRequestService;
    private final ChangeApplicationMapper mapper;

    @Override
    public void changeStatus(ChangeStatusRequest request, StreamObserver<Empty> responseObserver) {
        applicationRequestService.changeStatus(mapper.toDto(request));

        responseObserver.onNext(Empty.newBuilder().build());
        responseObserver.onCompleted();
    }
}
