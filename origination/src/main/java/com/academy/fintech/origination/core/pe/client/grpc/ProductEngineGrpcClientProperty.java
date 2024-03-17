package com.academy.fintech.origination.core.pe.client.grpc;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "origination.client.product-engine.grpc")
public record ProductEngineGrpcClientProperty(String host, int port) {
}
