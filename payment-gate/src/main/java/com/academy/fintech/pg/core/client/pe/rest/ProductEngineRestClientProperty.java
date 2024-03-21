package com.academy.fintech.pg.core.client.pe.rest;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "payment-gate.rest.pe")
public record ProductEngineRestClientProperty(String host, int port) {
}
