package com.eecs4413.groupe.userservice.exception;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NotEnoughPointsException extends RuntimeException {

    public NotEnoughPointsException(int expectedQuantity, int actualQuantity) {
        super(String.format("User does not have enough points (%d/%d)", expectedQuantity, actualQuantity));
    }
}