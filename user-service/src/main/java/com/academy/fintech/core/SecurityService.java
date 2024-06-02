package com.academy.fintech.core;

import com.academy.fintech.core.db.user.User;
import com.academy.fintech.core.db.user.UserService;
import com.academy.fintech.core.exception.AuthorizationException;
import com.academy.fintech.dto.AuthenticationRequest;
import com.academy.fintech.dto.RefreshRequest;
import com.academy.fintech.dto.TokenPair;
import com.academy.fintech.dto.RegistrationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SecurityService {

    private final UserService userService;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    public TokenPair completeRegistration(RegistrationRequest request) {
        User user = userService.create(request);
        return tokenService.generatePair(user.getUsername());
    }

    public TokenPair completeAuthorization(AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.username(), request.password()));
        } catch (BadCredentialsException e) {
            throw new AuthorizationException("Authorization failed");
        }
        return tokenService.generatePair(request.username());
    }

    public TokenPair refreshPair(RefreshRequest request) {
        return tokenService.refreshPair(request.refreshToken());
    }
}
