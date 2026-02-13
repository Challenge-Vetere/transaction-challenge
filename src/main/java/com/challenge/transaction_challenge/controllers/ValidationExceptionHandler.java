package com.challenge.transaction_challenge.controllers;

import com.challenge.transaction_challenge.exception.ParentNotFoundException;
import com.challenge.transaction_challenge.exception.TransactionNotFoundException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ValidationExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler({TransactionNotFoundException.class, ParentNotFoundException.class})
    public ResponseEntity<?> handleIllegalArgumentExceptions(RuntimeException ex) {
        return ResponseEntity.badRequest().body(Map.of("status", "error: " + ex.getLocalizedMessage()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleInvalidFormat(HttpMessageNotReadableException ex) {

        String message = "Request contains invalid data";

        if (ex.getCause() instanceof InvalidFormatException) {
            InvalidFormatException invalidFormat = (InvalidFormatException) ex.getCause();
            String field = invalidFormat.getPath().get(0).getFieldName();
            Object value = invalidFormat.getValue();
            String targetType = invalidFormat.getTargetType().getSimpleName();

            message = String.format(
                    "error: field '%s' has invalid value '%s'. Expected type: %s",
                    field, value, targetType
            );
        }

        return ResponseEntity.badRequest().body(Map.of("status", message));
    }
}

