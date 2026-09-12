package com.ecommerce.marketplace.exceptions;

public class userAlreadyExistsException extends RuntimeException {
    public userAlreadyExistsException(String message) {
        super(message);
    }
}
