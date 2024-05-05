package com.academy.fintech.pg.core.client.provider.merchant.rest;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("payment-gate.rest.provider.merchant")
public record MerchantProviderRestClientProperty(String host, int port) {
}
