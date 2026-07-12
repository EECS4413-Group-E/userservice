package com.eecs4413.groupe.userservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EmailNotUniqueException extends RuntimeException {
    public EmailNotUniqueException(String email) { super(String.format("User with email %s already exists", email)); }
}
