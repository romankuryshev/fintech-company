package com.academy.fintech.dto;

public record RegistrationRequest(
        String firstName,
        String lastName,
        String email,
        String salary,
        String password
) {
}
