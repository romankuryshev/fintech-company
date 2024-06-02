package com.academy.fintech.dto;

public record AuthenticationRequest(
        String username,
        String password
) {
}
