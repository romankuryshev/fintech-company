package com.academy.fintech.api.core.userservice.client;

import com.academy.fintech.api.public_interface.application.dto.AuthenticationRequest;
import com.academy.fintech.api.public_interface.application.dto.RefreshRequest;
import com.academy.fintech.api.public_interface.application.dto.RegistrationRequest;
import com.academy.fintech.api.public_interface.application.dto.TokenPair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class UserServiceRestClient {

    private final WebClient webClient;
    private final String apiKey;

    public UserServiceRestClient(UserServiceRestClientProperty property) {
        this.webClient = WebClient.create(property.host() + ":" + property.port());
        this.apiKey = property.apiKey();
    }

    public TokenPair registration(RegistrationRequest request) {
        return webClient.post()
                .uri("/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-API-KEY", apiKey)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(TokenPair.class)
                .block();
    }

    public TokenPair refreshToken(RefreshRequest request) {
        return webClient.post()
                .uri("/refresh-token")
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-API-KEY", apiKey)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(TokenPair.class)
                .block();
    }

    public TokenPair authentication(AuthenticationRequest request) {
        return webClient.post()
                .uri("/authentication")
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-API-KEY", apiKey)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(TokenPair.class)
                .block();
    }
}
