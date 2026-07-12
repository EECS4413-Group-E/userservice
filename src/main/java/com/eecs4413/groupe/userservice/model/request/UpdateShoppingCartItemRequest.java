package com.eecs4413.groupe.userservice.model.request;

import jakarta.validation.constraints.Min;

public record UpdateShoppingCartItemRequest(

        @Min(
                value = 1,
                message = "Quantity must be at least 1"
        )
        int quantity

) {
}