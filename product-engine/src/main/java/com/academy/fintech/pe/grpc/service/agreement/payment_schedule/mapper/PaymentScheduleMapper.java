package com.academy.fintech.pe.grpc.service.agreement.payment_schedule.mapper;

import com.academy.fintech.pe.core.service.agreement.db.payment_schedule.PaymentSchedule;
import com.academy.fintech.pe.grpc.service.agreement.payment_schedule.PaymentScheduleRequest;
import com.academy.fintech.pe.grpc.service.agreement.payment_schedule.PaymentScheduleResponse;
import com.academy.fintech.pe.grpc.service.agreement.payment_schedule.dto.PaymentScheduleRequestDto;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", uses = LoanPaymentMapper.class,
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PaymentScheduleMapper {

    PaymentScheduleRequestDto toDto(PaymentScheduleRequest request);

    @Mapping(target = "paymentsList", source = "paymentSchedule.payments")
    @Mapping(target = "agreementId", source = "paymentSchedule.agreement.id")
    PaymentScheduleResponse toResponse(PaymentSchedule paymentSchedule);
}
