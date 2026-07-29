package com.eecs4413.groupe.userservice.client.model.response;

import com.eecs4413.groupe.userservice.model.enums.Size;

import java.util.UUID;

public record ProductExistenceResponse(
        UUID productId,
        Size size,
        boolean exists
) {}
