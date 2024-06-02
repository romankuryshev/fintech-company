package com.academy.fintech.rest;

import com.academy.fintech.core.SecurityService;
import com.academy.fintech.dto.AuthenticationRequest;
import com.academy.fintech.dto.RefreshRequest;
import com.academy.fintech.dto.RegistrationRequest;
import com.academy.fintech.dto.TokenPair;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final SecurityService securityService;

    @PostMapping("/registration")
    public TokenPair registration(@RequestBody RegistrationRequest request) {
        return securityService.completeRegistration(request);
    }

    @PostMapping("/authentication")
    public TokenPair authorization(@RequestBody AuthenticationRequest request) {
        return securityService.completeAuthorization(request);
    }

    @PostMapping("/refresh-token")
    public TokenPair refreshToken(@RequestBody RefreshRequest request) {
        return securityService.refreshPair(request);
    }
}
