package com.academy.fintech.origination.core.pe.client;

import com.academy.fintech.origination.core.pe.client.grpc.ProductEngineGrpcClient;
import com.academy.fintech.origination.core.service.application.db.application.Application;
import com.academy.fintech.pe.grpc.service.agreement.agreement.AgreementRequest;
import com.academy.fintech.pe.grpc.service.agreement.agreement.AgreementResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ProductEngineClientService {
    private static final String DEFAULT_PRODUCT_CODE = "CL1.0";
    private static final BigDecimal DEFAULT_INTEREST = BigDecimal.valueOf(8);
    private static final int DEFAULT_TERM_IN_MONTHS = 12;
    private static final BigDecimal DEFAULT_ORIGINATION_AMOUNT = BigDecimal.valueOf(2100);

    private final ProductEngineGrpcClient productEngineGrpcClient;

    public UUID createAgreement(Application application) {
        AgreementRequest request = mapToAgreementRequest(application);
        AgreementResponse response = productEngineGrpcClient.createAgreement(request);

        return mapAgreementResponseToUuid(response);
    }

    private AgreementRequest mapToAgreementRequest(Application application) {
        return AgreementRequest.newBuilder()
                .setProductCode(DEFAULT_PRODUCT_CODE)
                .setClientId(application.getClient().getId().toString())
                .setInterest(DEFAULT_INTEREST.toString())
                .setTerm(DEFAULT_TERM_IN_MONTHS)
                .setDisbursementAmount(application.getRequestDisbursementAmount().toString())
                .setOriginationAmount(DEFAULT_ORIGINATION_AMOUNT.toString())
                .build();
    }

    private UUID mapAgreementResponseToUuid(AgreementResponse response) {
        return UUID.fromString(response.getId());
    }
}
