package com.eecs4413.groupe.userservice.model.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record AddShoppingCartItemRequest(

        @NotNull(message = "Product ID is required")
        UUID productId,

        @NotBlank(message = "Size is required")
        @Size(
                max = 20,
                message = "Size cannot exceed 20 characters"
        )
        String size,

        @Min(
                value = 1,
                message = "Quantity must be at least 1"
        )
        int quantity

) {
}