package com.ecommerce.marketplace.exceptions;

public class cartAlreadyExistsException extends RuntimeException {
    public cartAlreadyExistsException(String message) {
        super(message);
    }
}
