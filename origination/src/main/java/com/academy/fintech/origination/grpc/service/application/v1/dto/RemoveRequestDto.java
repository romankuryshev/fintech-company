package com.academy.fintech.origination.grpc.service.application.v1.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record RemoveRequestDto(
        @NotNull
        UUID applicationId
) {
}
