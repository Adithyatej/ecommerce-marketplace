package com.ecommerce.marketplace.exceptions;

public class productAlreadyExistsException extends RuntimeException {
    public productAlreadyExistsException(String message) {
        super(message);
    }
}
