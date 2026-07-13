package com.eecs4413.groupe.userservice.exception;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidShoppingCartQuantityException extends RuntimeException {

    public InvalidShoppingCartQuantityException(int quantity) {
        super(String.format("Shopping cart quantity must be at least 1. Received: %d", quantity));
    }
}