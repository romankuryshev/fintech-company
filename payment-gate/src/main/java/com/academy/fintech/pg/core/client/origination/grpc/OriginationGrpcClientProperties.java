package com.academy.fintech.pg.core.client.origination.grpc;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "payment-gate.grpc.origination")
public record OriginationGrpcClientProperties(String host, int port) {
}
