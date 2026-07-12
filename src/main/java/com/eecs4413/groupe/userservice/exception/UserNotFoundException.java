package com.eecs4413.groupe.userservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(UUID id) { super(String.format("User with id %s not found", id.toString())); }

}
