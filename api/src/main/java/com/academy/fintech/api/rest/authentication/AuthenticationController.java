package com.academy.fintech.api.rest.authentication;

import com.academy.fintech.api.core.userservice.AuthenticationService;
import com.academy.fintech.api.public_interface.application.dto.AuthenticationRequest;
import com.academy.fintech.api.public_interface.application.dto.RefreshRequest;
import com.academy.fintech.api.public_interface.application.dto.RegistrationRequest;
import com.academy.fintech.api.public_interface.application.dto.TokenPair;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/registration")
    public TokenPair registration(@RequestBody RegistrationRequest request) {
        return authenticationService.registration(request);
    }

    @PostMapping("/authentication")
    public TokenPair authentication(@RequestBody AuthenticationRequest request) {
        return authenticationService.authentication(request);
    }

    @PostMapping("/refresh-token")
    public TokenPair refreshToken(@RequestBody RefreshRequest request) {
        return authenticationService.refreshToken(request);
    }
}
