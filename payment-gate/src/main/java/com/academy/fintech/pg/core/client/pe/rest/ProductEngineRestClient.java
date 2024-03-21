package com.academy.fintech.pg.core.client.pe.rest;

import com.academy.fintech.pg.public_interface.PaymentDto;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class ProductEngineRestClient {

    private static final String CREATE_PAYMENT_URI = "/payments/create";

    private final WebClient webClient;

    public ProductEngineRestClient(ProductEngineRestClientProperty property) {
        this.webClient = WebClient.create(property.host() + ":" + property.port());
    }

    public void createPayment(PaymentDto paymentDto) {
        webClient.post()
                .uri(CREATE_PAYMENT_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(paymentDto)
                .retrieve()
                .toBodilessEntity()
                .block();
    }
}
