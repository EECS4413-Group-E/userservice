package com.eecs4413.groupe.userservice.client.model.request;

import com.eecs4413.groupe.userservice.model.enums.Size;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ProductExistenceRequest(
        @NotNull(message = "Product ID cannot be empty")
        UUID productId,

        @NotNull(message = "Size cannot be empty")
        Size size
) {
}