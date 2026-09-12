package com.ecommerce.marketplace.exceptions;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class globalExceptionHandler {

    @ExceptionHandler(userAlreadyExistsException.class)
    public ResponseEntity<?> handleUserAlreadyExistsException(userAlreadyExistsException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(IdNotFoundException.class)
    public ResponseEntity<?> handleIdNotFoundException(IdNotFoundException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(categoryNotFoundException.class)
    public ResponseEntity<?> handleCategoryNotFoundException(categoryNotFoundException ex) {
        return ResponseEntity.status(404).body(ex.getMessage());
    }

    @ExceptionHandler(productAlreadyExistsException.class)
    public ResponseEntity<?> handleProductAlreadyExists(productAlreadyExistsException ex) {
        return ResponseEntity.status(400).body(ex.getMessage());
    }

    @ExceptionHandler(productNotFoundException.class)
    public ResponseEntity<?> handleProductNotFound(productNotFoundException ex) {
        return ResponseEntity.status(404).body(ex.getMessage());
    }

    @ExceptionHandler(cartNotFoundException.class)
    public ResponseEntity<?> handleCartNotFound(productNotFoundException ex) {
        return ResponseEntity.status(404).body(ex.getMessage());
    }

    @ExceptionHandler(cartAlreadyExistsException.class)
    public ResponseEntity<?> handleCartAlreadyExists(productNotFoundException ex) {
        return ResponseEntity.status(404).body(ex.getMessage());
    }
}
