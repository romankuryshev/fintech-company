package com.academy.fintech.api.core.userservice;

import com.academy.fintech.api.core.userservice.client.UserServiceRestClient;
import com.academy.fintech.api.public_interface.application.dto.AuthenticationRequest;
import com.academy.fintech.api.public_interface.application.dto.RefreshRequest;
import com.academy.fintech.api.public_interface.application.dto.RegistrationRequest;
import com.academy.fintech.api.public_interface.application.dto.TokenPair;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserServiceRestClient restClient;

    public TokenPair registration(RegistrationRequest request) {
        return restClient.registration(request);
    }

    public TokenPair authentication(AuthenticationRequest request) {
        return restClient.authentication(request);
    }

    public TokenPair refreshToken(RefreshRequest request) {
        return restClient.refreshToken(request);
    }
}
