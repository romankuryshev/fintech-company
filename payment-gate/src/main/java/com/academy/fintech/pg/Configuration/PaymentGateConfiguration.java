package com.academy.fintech.pg.Configuration;

import com.academy.fintech.pg.core.disbursement.provider.merchant.rest.MerchantProviderRestClientProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({
        MerchantProviderRestClientProperty.class
})
public class PaymentGateConfiguration {
}
