package com.eecs4413.groupe.userservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CatalogueServiceException extends RuntimeException {

    public CatalogueServiceException() {
        super("Unable to communicate with Catalogue Service");
    }

    public CatalogueServiceException(int status, String error) {
        super(String.format("Call to Catalogue Service failed with response code of %d and message: %s", status, error));
    }
}
