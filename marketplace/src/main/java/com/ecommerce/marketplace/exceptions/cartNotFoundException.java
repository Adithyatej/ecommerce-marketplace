package com.ecommerce.marketplace.exceptions;

public class cartNotFoundException extends RuntimeException {
    public cartNotFoundException(String message) {
        super(message);
    }
}
