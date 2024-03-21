package com.academy.fintech.pg.core.client.provider.merchant.rest;

import com.academy.fintech.pg.core.client.provider.merchant.dto.DisbursementStatusRequest;
import com.academy.fintech.pg.core.client.provider.merchant.dto.DisbursementStatusResponse;
import com.academy.fintech.pg.public_interface.DisbursementDto;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class MerchantProviderRestClient {

    private static final String DISBURSEMENT_CREATE_URI = "/disbursement/create";
    private static final String CHECK_DISBURSEMENT_URI = "/disbursement/check/status";
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

    public DisbursementStatusResponse checkDisbursement(DisbursementStatusRequest request) {
        return webClient.post()
                .uri(CHECK_DISBURSEMENT_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(DisbursementStatusResponse.class)
                .block();
    }
}
