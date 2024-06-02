package com.academy.fintech.api.public_interface.application.dto;

public record TokenPair(
        String token,
        String refreshToken
) {
}
