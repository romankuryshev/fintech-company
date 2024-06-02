package com.academy.fintech.api.configuration;

import com.academy.fintech.api.core.origination.client.grpc.OriginationGrpcClientProperty;
import com.academy.fintech.api.core.userservice.client.UserServiceRestClientProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({ OriginationGrpcClientProperty.class, UserServiceRestClientProperty.class })
public class ApiConfiguration { }
