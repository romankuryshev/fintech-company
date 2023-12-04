package com.academy.fintech.pe.core.service.agreement.exception;

public class ProductDoesNotExists extends RuntimeException{
    public ProductDoesNotExists(String message) {
        super(message);
    }
}
