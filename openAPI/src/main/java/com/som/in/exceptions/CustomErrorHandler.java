package com.som.in.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class CustomErrorHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<RestResponseException> handleGenericException(Exception exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(getErrorResponse(ExceptionMessage.INTERNAL_SERVER_ERROR.getMessage(),
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        ExceptionMessage.INTERNAL_SERVER_ERROR.getErrorCode()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomEntryException.class)
    public ResponseEntity<RestResponseException> handleCustomEntryException(CustomEntryException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(getErrorResponse(exception.getMessage(),
                        HttpStatus.BAD_REQUEST.value(),
                        ExceptionMessage.USER_NOT_FOUND.getErrorCode()));
    }

    public static RestResponseException getErrorResponse(String message, Integer statusCode, String errorCode) {
        return RestResponseException.builder().message(message).status(statusCode).build();
    }
}
