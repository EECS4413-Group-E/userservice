package com.eecs4413.groupe.userservice.model.response;

import java.util.UUID;

public record ShoppingCartItemResponse(
        UUID id,
        UUID productId,
        String size,
        int quantity
) {
}