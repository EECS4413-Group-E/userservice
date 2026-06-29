package com.eecs4413.groupe.userservice.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private record ErrorResponse(String error) {}

    @ExceptionHandler(EmailNotUniqueException.class)
    public ResponseEntity<ErrorResponse> handleEmailNotUnique(
            EmailNotUniqueException ex) {

        return ResponseEntity
                .badRequest()
                .body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(
            UserNotFoundException ex) {

        return ResponseEntity
                .badRequest()
                .body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(ConstraintViolationException ex) {
        StringBuilder stringBuilder = new StringBuilder();
        for (ConstraintViolation cv : ex.getConstraintViolations()) {
            stringBuilder.append(cv.getMessage());
            stringBuilder.append("; ");
        }

        stringBuilder.delete(stringBuilder.length()-2, stringBuilder.length()); // Remove last "; "

        return ResponseEntity
                .badRequest()
                .body(new ErrorResponse(stringBuilder.toString()));
    }

}
