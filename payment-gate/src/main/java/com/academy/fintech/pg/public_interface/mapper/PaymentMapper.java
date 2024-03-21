package com.academy.fintech.pg.public_interface.mapper;

import com.academy.fintech.pg.core.payments.db.Payment;
import com.academy.fintech.pg.public_interface.PaymentDto;
import com.academy.fintech.pg.rest.payments.CreatePaymentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PaymentMapper {

    CreatePaymentResponse toCreatePaymentResponse(Payment payment);
    PaymentDto toDto(Payment payment);
}
