package com.academy.fintech.scoring.core.processing.exception;

public class ProductDoesNotExists extends RuntimeException{
    public ProductDoesNotExists(String message) {
        super(message);
    }
}
