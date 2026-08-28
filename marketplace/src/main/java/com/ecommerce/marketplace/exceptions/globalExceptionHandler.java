package com.ecommerce.marketplace.exceptions;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class globalExceptionHandler {

    @ExceptionHandler(userAlreadyExistsException.class)
    public ResponseEntity<String> handleUserAlreadyExistsException(userAlreadyExistsException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(IdNotFoundException.class)
    public ResponseEntity<String> handleIdNotFoundException(IdNotFoundException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(categoryNotFoundException.class)
    public ResponseEntity<String> handleCategoryNotFoundException(categoryNotFoundException ex) {
        return ResponseEntity.status(404).body(ex.getMessage());
    }
}
