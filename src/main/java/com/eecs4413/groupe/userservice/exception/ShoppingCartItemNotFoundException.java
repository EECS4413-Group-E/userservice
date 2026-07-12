package com.eecs4413.groupe.userservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.eecs4413.groupe.userservice.model.enums.Size;

import java.util.UUID;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ShoppingCartItemNotFoundException
        extends RuntimeException {

    public ShoppingCartItemNotFoundException(
            UUID userId,
            UUID productId,
            Size size
    ) {
        super(String.format(
                "Shopping cart item for user %s, product %s, and size %s was not found",
                userId,
                productId,
                size
        ));
    }
}