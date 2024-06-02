package com.academy.fintech.api.public_interface.application.dto;

public record AuthenticationRequest(
        String username,
        String password
) {
}
