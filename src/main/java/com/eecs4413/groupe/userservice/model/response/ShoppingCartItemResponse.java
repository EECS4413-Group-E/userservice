package com.eecs4413.groupe.userservice.model.response;

import java.util.UUID;

import com.eecs4413.groupe.userservice.model.entity.ShoppingCartItem;
import com.eecs4413.groupe.userservice.model.enums.Size;

public record ShoppingCartItemResponse(
        UUID id,
        UUID productId,
        Size size,
        int quantity
) {
	public static ShoppingCartItemResponse from(
            ShoppingCartItem shoppingCartItem
    ) {
        return new ShoppingCartItemResponse(
                shoppingCartItem.getId(),
                shoppingCartItem.getProductId(),
                shoppingCartItem.getSize(),
                shoppingCartItem.getQuantity()
        );
    }
}