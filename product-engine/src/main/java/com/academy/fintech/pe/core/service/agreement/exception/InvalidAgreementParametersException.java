package com.academy.fintech.pe.core.service.agreement.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class InvalidAgreementParametersException extends RuntimeException {

    private final List<String> messages;

    public InvalidAgreementParametersException(List<String> messages) {
        this.messages = messages;
    }

    public String getMessagesAsString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Invalid agreement parameters in request:\n");
        for (String message : messages) {
            builder.append(message).append("\n");
        }
        return builder.toString();
    }
}
