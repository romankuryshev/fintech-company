package com.academy.fintech.pe.grpc.service.agreement.agreement;

import com.academy.fintech.pe.core.service.agreement.AgreementCreationService;
import com.academy.fintech.pe.core.service.agreement.db.agreement.Agreement;
import com.academy.fintech.pe.grpc.service.agreement.agreement.dto.AgreementDto;
import com.academy.fintech.pe.grpc.service.agreement.agreement.mapper.AgreementMapper;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class AgreementController extends AgreementCreationServiceGrpc.AgreementCreationServiceImplBase {

    private final AgreementCreationService agreementCreationService;

    private final AgreementMapper mapper;

    public AgreementController(AgreementCreationService agreementCreationService, AgreementMapper mapper) {
        this.agreementCreationService = agreementCreationService;
        this.mapper = mapper;
    }

    @Override
    public void create(AgreementRequest request, StreamObserver<AgreementResponse> responseObserver) {
        AgreementDto agreementDto = mapper.requestToDto(request);

        Agreement resultAgreement = agreementCreationService.createAgreement(agreementDto);
        AgreementResponse response = mapper.toResponse(resultAgreement);

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
