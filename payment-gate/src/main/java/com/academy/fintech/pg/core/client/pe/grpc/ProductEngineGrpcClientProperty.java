package com.academy.fintech.pg.core.client.pe.grpc;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "payment-gate.grpc.pe")
public record ProductEngineGrpcClientProperty(String host, int port) {
}
