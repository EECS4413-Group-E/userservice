package com.eecs4413.groupe.userservice.exception;

import java.util.UUID;

public class EmailNotUniqueException extends RuntimeException {
    public EmailNotUniqueException(String email) { super(String.format("User with email %s already exists", email)); }
}
