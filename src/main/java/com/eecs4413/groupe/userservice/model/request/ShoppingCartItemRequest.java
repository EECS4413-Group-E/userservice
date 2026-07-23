package com.eecs4413.groupe.userservice.model.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

import com.eecs4413.groupe.userservice.model.enums.Size;

public record ShoppingCartItemRequest(

        @NotNull(message = "Product ID is required")
        UUID productId,

        @NotNull(message = "Size is required")
        Size size,

        @Min(value = 1, message = "Quantity must be at least 1")
        int quantity

) {}