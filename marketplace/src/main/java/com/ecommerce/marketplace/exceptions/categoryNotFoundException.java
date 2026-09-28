package com.ecommerce.marketplace.exceptions;

public class categoryNotFoundException extends RuntimeException {
    public categoryNotFoundException(String message) {
        super(message);
    }
}
