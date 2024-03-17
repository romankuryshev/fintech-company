package com.academy.fintech.pg.grpc.disbursement;

import com.academy.fintech.pg.core.disbursement.DisbursementExecutionService;
import com.academy.fintech.pg.public_interface.mapper.DisbursementMapper;
import com.academy.fintech.pg.payment_service.DisbursementRequest;
import com.academy.fintech.pg.payment_service.DisbursementServiceGrpc;
import com.academy.fintech.pg.payment_service.Empty;
import io.grpc.stub.StreamObserver;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

@Slf4j
@GrpcService
@AllArgsConstructor
public class DisbursementController extends DisbursementServiceGrpc.DisbursementServiceImplBase {

    private final DisbursementExecutionService disbursementExecutionService;

    private final DisbursementMapper mapper;

    @Override
    public void executeDisbursement(DisbursementRequest request, StreamObserver<Empty> responseObserver) {
        disbursementExecutionService.executeDisbursement(mapper.requestToDto(request));

        responseObserver.onNext(Empty.newBuilder().build());
        responseObserver.onCompleted();
    }
}
