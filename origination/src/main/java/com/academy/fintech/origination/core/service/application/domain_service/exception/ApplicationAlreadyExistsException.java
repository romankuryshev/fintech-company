package com.academy.fintech.origination.core.service.application.domain_service.exception;

import lombok.Getter;

import java.util.UUID;

@Getter
public class ApplicationAlreadyExistsException extends RuntimeException{

    private final UUID applicationId;

    public ApplicationAlreadyExistsException(String message, UUID applicationId) {
        super(message);
        this.applicationId = applicationId;
    }
}
