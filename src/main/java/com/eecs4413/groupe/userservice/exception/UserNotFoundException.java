package com.eecs4413.groupe.userservice.exception;

import java.util.UUID;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(UUID id) { super(String.format("User with ID: %s not found", id.toString())); }

}
