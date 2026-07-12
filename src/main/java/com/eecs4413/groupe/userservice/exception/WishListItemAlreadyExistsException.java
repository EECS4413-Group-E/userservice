package com.eecs4413.groupe.userservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class WishListItemAlreadyExistsException extends RuntimeException {
    public WishListItemAlreadyExistsException(UUID userId, UUID listingId) {
        super(String.format("Listing with ID: %s is already in wishlist for user with ID: %s",
                listingId,
                userId
        ));
    }
}