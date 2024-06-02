package com.academy.fintech.api.core.userservice.client;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("user-service")
public record UserServiceRestClientProperty(String host, int port, String apiKey)  {
}
