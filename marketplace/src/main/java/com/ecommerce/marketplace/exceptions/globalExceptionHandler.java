package com.ecommerce.marketplace.exceptions;


import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class globalExceptionHandler {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new LinkedHashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return ResponseEntity.status(400).body(errors.entrySet().stream().findFirst());
    }

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

    @ExceptionHandler(AddressNotFoundException.class)
    public ResponseEntity<?> handleAddressNotFound(AddressNotFoundException ex) {
        return ResponseEntity.status(404).body(ex.getMessage());
    }

    @ExceptionHandler(ProductOutOfStockException.class)
    public ResponseEntity<?> handleProductOutOfStockException(ProductOutOfStockException ex) {
        return ResponseEntity.status(400).body(ex.getMessage());
    }


    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<?> handleOrderNotFoundException(OrderNotFoundException ex) {
        return ResponseEntity.status(404).body(ex.getMessage());
    }

    @ExceptionHandler(NocancellationPolicyException.class)
    public ResponseEntity<?> handleNoCancellationPolicyException(NocancellationPolicyException ex) {
        return ResponseEntity.status(400).body(ex.getMessage());
    }
}
