package com.academy.fintech.pg.Configuration;

import com.academy.fintech.pg.core.client.origination.grpc.OriginationGrpcClientProperties;
import com.academy.fintech.pg.core.client.pe.grpc.ProductEngineGrpcClientProperty;
import com.academy.fintech.pg.core.client.provider.merchant.rest.MerchantProviderRestClientProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@EnableConfigurationProperties({
        MerchantProviderRestClientProperty.class,
        ProductEngineGrpcClientProperty.class,
        OriginationGrpcClientProperties.class
})
public class PaymentGateConfiguration {
}
