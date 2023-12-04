package com.academy.fintech.pe.grpc.service.agreement.statistic;

import com.academy.fintech.pe.core.service.agreement.exception.ProductDoesNotExists;
import io.grpc.Status;
import io.grpc.StatusException;
import net.devh.boot.grpc.server.advice.GrpcAdvice;
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler;

import static io.grpc.Status.INVALID_ARGUMENT;

@GrpcAdvice
public class StatisticExceptionHandler {

    @GrpcExceptionHandler(ProductDoesNotExists.class)
    public StatusException handleProductDoesNotExistsException(ProductDoesNotExists e) {
        Status status = INVALID_ARGUMENT.withDescription(e.getMessage()).withCause(e);
        return status.asException();
    }
}
