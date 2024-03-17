package com.academy.fintech.origination.core.pg.client;

import com.academy.fintech.origination.core.service.application.db.application.Application;
import com.academy.fintech.pg.payment_service.DisbursementRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PaymentServiceMapper {

    @Mapping(target = "agreementId", source = "agreementId")
    @Mapping(target = "amount", source = "requestDisbursementAmount")
    DisbursementRequest toPaymentRequest(Application application);
}
