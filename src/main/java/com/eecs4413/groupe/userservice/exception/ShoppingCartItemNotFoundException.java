package com.eecs4413.groupe.userservice.exception;

import java.util.UUID;

public class ShoppingCartItemNotFoundException
        extends RuntimeException {

    public ShoppingCartItemNotFoundException(
            UUID userId,
            UUID productId,
            String size
    ) {
        super(
                "Shopping cart item was not found for user "
                        + userId
                        + ", product "
                        + productId
                        + ", and size "
                        + size
        );
    }
}