package com.academy.fintech.pe.grpc.service.agreement.payment_schedule;

import com.academy.fintech.pe.core.service.agreement.PaymentScheduleCreationService;
import com.academy.fintech.pe.core.service.agreement.db.payment_schedule.PaymentSchedule;
import com.academy.fintech.pe.grpc.service.agreement.payment_schedule.dto.PaymentScheduleRequestDto;
import com.academy.fintech.pe.grpc.service.agreement.payment_schedule.mapper.PaymentScheduleMapper;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.Optional;

@GrpcService
public class PaymentScheduleController extends PaymentScheduleCreationServiceGrpc.PaymentScheduleCreationServiceImplBase {

    private final PaymentScheduleCreationService paymentScheduleCreationService;

    private final PaymentScheduleMapper mapper;

    public PaymentScheduleController(PaymentScheduleCreationService paymentScheduleCreationService, PaymentScheduleMapper mapper) {
        this.paymentScheduleCreationService = paymentScheduleCreationService;
        this.mapper = mapper;
    }

    @Override
    public void createSchedule(PaymentScheduleRequest request, StreamObserver<PaymentScheduleResponse> responseObserver) {
        PaymentScheduleRequestDto scheduleDto = mapper.toDto(request);

        PaymentSchedule schedule = paymentScheduleCreationService.createSchedule(scheduleDto);
        PaymentScheduleResponse response = mapper.toResponse(schedule);

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
