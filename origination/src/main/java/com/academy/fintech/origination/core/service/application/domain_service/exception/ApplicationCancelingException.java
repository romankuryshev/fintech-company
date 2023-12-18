package com.academy.fintech.origination.core.service.application.domain_service.exception;

public class ApplicationCancelingException extends RuntimeException{
    public ApplicationCancelingException(String message) {
        super(message);
    }
}
