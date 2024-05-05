package com.academy.fintech.origination.core.pe.client.grpc;

import com.academy.fintech.pe.grpc.service.agreement.agreement.AgreementCreationServiceGrpc;
import com.academy.fintech.pe.grpc.service.agreement.agreement.AgreementCreationServiceGrpc.AgreementCreationServiceBlockingStub;
import com.academy.fintech.pe.grpc.service.agreement.agreement.AgreementRequest;
import com.academy.fintech.pe.grpc.service.agreement.agreement.AgreementResponse;
import com.academy.fintech.pe.grpc.service.agreement.payment_schedule.PaymentScheduleCreationServiceGrpc;
import com.academy.fintech.pe.grpc.service.agreement.payment_schedule.PaymentScheduleRequest;
import com.academy.fintech.pe.grpc.service.agreement.payment_schedule.PaymentScheduleResponse;
import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ProductEngineGrpcClient {
    private final AgreementCreationServiceBlockingStub agreementCreationServiceBlockingStub;
    private final PaymentScheduleCreationServiceGrpc.PaymentScheduleCreationServiceBlockingStub paymentScheduleCreationServiceBlockingStub;

    public ProductEngineGrpcClient(ProductEngineGrpcClientProperty property) {
        Channel channel = ManagedChannelBuilder.forAddress(property.host(), property.port())
                .usePlaintext()
                .build();
        this.agreementCreationServiceBlockingStub = AgreementCreationServiceGrpc.newBlockingStub(channel);
        this.paymentScheduleCreationServiceBlockingStub = PaymentScheduleCreationServiceGrpc.newBlockingStub(channel);
    }

    public AgreementResponse createAgreement(AgreementRequest request) throws StatusRuntimeException {
        try {
            return agreementCreationServiceBlockingStub.create(request);
        } catch (StatusRuntimeException e) {
            log.error("Got error from Product Engine by request: {}", request, e);
            throw e;
        }
    }

    public PaymentScheduleResponse createSchedule(PaymentScheduleRequest request) {
        try {
            return paymentScheduleCreationServiceBlockingStub.createSchedule(request);
        }catch (StatusRuntimeException e) {
            log.error("Got error from Product Engine by request: {}", request, e);
            throw e;
        }
    }
}
