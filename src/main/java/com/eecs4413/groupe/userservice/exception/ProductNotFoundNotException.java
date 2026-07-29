package com.eecs4413.groupe.userservice.exception;

import com.eecs4413.groupe.userservice.model.enums.Size;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ProductNotFoundNotException extends RuntimeException{
    public ProductNotFoundNotException(UUID productId, Size size) { super(String.format("Product with id %s and size %s not found", productId.toString(), size.toString())); }

}
