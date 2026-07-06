package com.eecs4413.groupe.userservice.exception;

import java.util.UUID;

public class WishListItemAlreadyExistsException extends RuntimeException {
    public WishListItemAlreadyExistsException(UUID userId, UUID listingId) {
        super(String.format("Listing with ID: %s is already in wishlist for user with ID: %s",
                listingId,
                userId
        ));
    }
}