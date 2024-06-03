package com.academy.fintech.origination.core.pe.client;

import com.academy.fintech.origination.core.pe.client.grpc.ProductEngineGrpcClient;
import com.academy.fintech.origination.core.service.application.db.application.Application;
import com.academy.fintech.origination.grpc.service.disbursement.dto.ChangeApplicationStatusDto;
import com.academy.fintech.pe.grpc.service.agreement.agreement.AgreementRequest;
import com.academy.fintech.pe.grpc.service.agreement.agreement.AgreementResponse;
import com.academy.fintech.pe.grpc.service.agreement.payment_schedule.PaymentScheduleRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ProductEngineClientService {
    private static final BigDecimal DEFAULT_ORIGINATION_AMOUNT = BigDecimal.valueOf(2100);

    private final ProductEngineGrpcClient productEngineGrpcClient;

    public UUID createAgreement(Application application) {
        AgreementRequest request = mapToAgreementRequest(application);
        AgreementResponse response = productEngineGrpcClient.createAgreement(request);

        return mapAgreementResponseToUuid(response);
    }

    public void activateAgreementAndCreateSchedule(ChangeApplicationStatusDto dto) {
        productEngineGrpcClient.createSchedule(mapToPaymentRequest(dto));
    }

    private AgreementRequest mapToAgreementRequest(Application application) {
        return AgreementRequest.newBuilder()
                .setProductCode(application.getProductCode())
                .setClientId(application.getClient().getId().toString())
                .setInterest(application.getInterest().toString())
                .setTerm(application.getTermInMonths())
                .setDisbursementAmount(application.getRequestDisbursementAmount().toString())
                .setOriginationAmount(DEFAULT_ORIGINATION_AMOUNT.toString())
                .build();
    }

    private UUID mapAgreementResponseToUuid(AgreementResponse response) {
        return UUID.fromString(response.getId());
    }

    private PaymentScheduleRequest mapToPaymentRequest(ChangeApplicationStatusDto dto) {
        return PaymentScheduleRequest.newBuilder()
                .setAgreementId(dto.agreementId().toString())
                .setDisbursementDate(dto.disbursementDate().toString())
                .build();
    }
}
