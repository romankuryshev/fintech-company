package com.academy.fintech.pe.grpc.service.agreement.payment_schedule.mapper;


import com.academy.fintech.pe.core.service.agreement.db.loan_payment.LoanPayment;
import com.academy.fintech.pe.grpc.service.agreement.payment_schedule.LoanPaymentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LoanPaymentMapper {
    @Mapping(target = "id", source = "paymentId")
    @Mapping(target = "date", source = "paymentDate")
    @Mapping(target = "period", source = "periodNumber")
    LoanPaymentResponse toResponse(LoanPayment loanPayment);
    List<LoanPaymentResponse> toResponseList(List<LoanPayment> loanPayments);
}
