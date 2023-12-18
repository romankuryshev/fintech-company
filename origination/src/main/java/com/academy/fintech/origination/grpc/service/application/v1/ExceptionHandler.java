package com.academy.fintech.origination.grpc.service.application.v1;

import com.academy.fintech.origination.core.service.application.domain_service.exception.ApplicationAlreadyExistsException;
import com.academy.fintech.origination.core.service.application.domain_service.exception.ApplicationCancelingException;
import com.academy.fintech.origination.core.service.application.domain_service.exception.ApplicationNotFoundException;
import io.grpc.Metadata;
import io.grpc.Status;
import io.grpc.StatusException;
import net.devh.boot.grpc.server.advice.GrpcAdvice;
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler;

import static io.grpc.Status.ALREADY_EXISTS;
import static io.grpc.Status.CANCELLED;

@GrpcAdvice
public class ExceptionHandler {

    @GrpcExceptionHandler(ApplicationAlreadyExistsException.class)
    public StatusException handleApplicationAlreadyExistsException(ApplicationAlreadyExistsException e) {
        Status status = ALREADY_EXISTS.withDescription(e.getMessage()).withCause(e);

        Metadata metadata = new Metadata();
        metadata.put(Metadata.Key.of("applicationId", Metadata.ASCII_STRING_MARSHALLER), e.getApplicationId().toString());

        return status.asException(metadata);
    }

    @GrpcExceptionHandler(ApplicationNotFoundException.class)
    public StatusException handleApplicationNotFoundException(ApplicationNotFoundException e) {
        Status status = CANCELLED.withDescription(e.getMessage()).withCause(e);
        return status.asException();
    }

    @GrpcExceptionHandler(ApplicationCancelingException.class)
    public StatusException handleApplicationCancelingException(ApplicationCancelingException e) {
        Status status = CANCELLED.withDescription(e.getMessage()).withCause(e);
        return status.asException();
    }
}
