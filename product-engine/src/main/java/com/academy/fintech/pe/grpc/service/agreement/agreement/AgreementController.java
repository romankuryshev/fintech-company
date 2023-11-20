package com.academy.fintech.pe.grpc.service.agreement.agreement;

import com.academy.fintech.pe.core.service.agreement.AgreementCreationService;
import com.academy.fintech.pe.core.service.agreement.db.agreement.Agreement;
import com.academy.fintech.pe.grpc.service.agreement.agreement.dto.AgreementDto;
import com.academy.fintech.pe.grpc.service.agreement.agreement.mapper.AgreementMapper;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@GrpcService
public class AgreementController extends AgreementCreationServiceGrpc.AgreementCreationServiceImplBase {

    private final static String agreementIsNotValid = "error: The agreement does not meet the conditions.";

    private final static String ok = "ok";

    private final AgreementCreationService agreementCreationService;

    private final AgreementMapper mapper;

    @Autowired
    public AgreementController(AgreementCreationService agreementCreationService, AgreementMapper mapper) {
        this.agreementCreationService = agreementCreationService;
        this.mapper = mapper;
    }

    @Override
    public void create(AgreementRequest request, StreamObserver<AgreementResponse> responseObserver) {
        AgreementDto agreementDto = mapper.requestToDto(request);
        Optional<Agreement> resultAgreement = agreementCreationService.createAgreement(agreementDto);

        AgreementResponse response;
        if (resultAgreement.isPresent()) {
            response = mapper.toResponse(resultAgreement.get(), ok);
        } else {
            response = mapper.toResponse(null, agreementIsNotValid);
        }

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
