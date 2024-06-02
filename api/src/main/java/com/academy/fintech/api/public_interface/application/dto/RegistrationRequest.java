package com.academy.fintech.api.public_interface.application.dto;

public record RegistrationRequest(
        String firstName,
        String lastName,
        String email,
        String salary,
        String password
) {
}
