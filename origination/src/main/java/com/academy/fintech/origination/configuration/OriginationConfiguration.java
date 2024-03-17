package com.academy.fintech.origination.configuration;

import com.academy.fintech.origination.core.pe.client.grpc.ProductEngineGrpcClientProperty;
import com.academy.fintech.origination.core.pg.client.grpc.PaymentGateGrpcProperty;
import com.academy.fintech.origination.core.scoring.client.grpc.ScoringGrpcClientProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableAsync
@EnableScheduling
@EnableConfigurationProperties({
        ScoringGrpcClientProperty.class,
        PaymentGateGrpcProperty.class,
        ProductEngineGrpcClientProperty.class })
public class OriginationConfiguration {
}
