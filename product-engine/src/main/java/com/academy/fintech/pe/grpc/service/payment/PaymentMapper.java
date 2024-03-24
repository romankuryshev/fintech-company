package com.academy.fintech.pe.grpc.service.payment;

import com.academy.fintech.pe.grpc.service.payment.dto.Payment;
import com.academy.fintech.pg.payment_processing_service.ProcessPaymentRequest;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PaymentMapper {

    Payment toDto(ProcessPaymentRequest request);
}
