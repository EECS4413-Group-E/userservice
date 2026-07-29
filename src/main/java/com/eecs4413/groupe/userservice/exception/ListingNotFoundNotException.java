package com.eecs4413.groupe.userservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ListingNotFoundNotException extends RuntimeException{
    public ListingNotFoundNotException(UUID id) { super(String.format("Listing with id %s not found", id.toString())); }

}
