package com.academy.fintech.pg.core.client.provider.merchant.rest;

import com.academy.fintech.pg.core.client.provider.merchant.dto.DisbursementStatusResponse;
import com.academy.fintech.pg.public_interface.DisbursementDto;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.UUID;

@Component
public class MerchantProviderRestClient {

    private static final String DISBURSEMENT_CREATE_URI = "/disbursements";
    private static final String CHECK_DISBURSEMENT_URI = "/disbursements/%s/status";
    private final WebClient webClient;

    public MerchantProviderRestClient(MerchantProviderRestClientProperty property) {
        this.webClient = WebClient.create(property.host() + ":" + property.port());
    }

    public void sendDisbursement(DisbursementDto disbursementDto) {
        webClient.post()
                .uri(DISBURSEMENT_CREATE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(disbursementDto)
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    public DisbursementStatusResponse checkDisbursement(UUID agreementId) {
        return webClient.get()
                .uri(String.format(CHECK_DISBURSEMENT_URI, agreementId))
                .retrieve()
                .bodyToMono(DisbursementStatusResponse.class)
                .block();
    }
}
