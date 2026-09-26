package com.ecommerce.marketplace.exceptions;

public class productNotFoundException extends RuntimeException {
    public productNotFoundException(String message) {
        super(message);
    }
}
