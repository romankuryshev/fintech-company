package com.academy.fintech.pe.grpc.service.payment;

import com.academy.fintech.pe.core.service.account.PaymentProcessingService;
import com.academy.fintech.pg.payment_processing_service.Empty;
import com.academy.fintech.pg.payment_processing_service.PaymentProcessingServiceGrpc;
import com.academy.fintech.pg.payment_processing_service.ProcessPaymentRequest;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class PaymentController extends PaymentProcessingServiceGrpc.PaymentProcessingServiceImplBase {

    private final PaymentMapper paymentMapper;

    private final PaymentProcessingService paymentProcessingService;

    @Override
    public void process(ProcessPaymentRequest request, StreamObserver<Empty> responseObserver) {
        paymentProcessingService.processPayment(paymentMapper.toDto(request));
        responseObserver.onNext(Empty.newBuilder().build());
        responseObserver.onCompleted();
    }
}
