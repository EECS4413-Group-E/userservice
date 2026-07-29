package com.eecs4413.groupe.userservice.client.model.response;

import java.util.UUID;

public record ListingExistenceResponse(
        UUID listingId,
        boolean exists
) {}
