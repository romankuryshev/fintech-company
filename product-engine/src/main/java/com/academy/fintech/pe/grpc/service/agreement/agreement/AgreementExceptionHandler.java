package com.academy.fintech.pe.grpc.service.agreement.agreement;

import com.academy.fintech.pe.core.service.agreement.exception.InvalidAgreementParametersException;
import io.grpc.Status;
import io.grpc.StatusException;
import net.devh.boot.grpc.server.advice.GrpcAdvice;
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler;

import static io.grpc.Status.INVALID_ARGUMENT;

@GrpcAdvice
public class AgreementExceptionHandler {

    @GrpcExceptionHandler(InvalidAgreementParametersException.class)
    public StatusException handleApplicationAlreadyExistsException(InvalidAgreementParametersException e) {
        Status status = INVALID_ARGUMENT.withDescription(e.getMessagesAsString()).withCause(e);
        return status.asException();
    }
}
