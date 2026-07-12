package com.eecs4413.groupe.userservice.exception;

public class InvalidShoppingCartQuantityException
        extends RuntimeException {

    public InvalidShoppingCartQuantityException(int quantity) {
        super(
                "Shopping cart quantity must be at least 1. "
                        + "Received: "
                        + quantity
        );
    }
}