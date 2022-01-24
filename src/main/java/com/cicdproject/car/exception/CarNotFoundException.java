package com.cicdproject.car.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception class for car not found.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class CarNotFoundException extends RuntimeException{

    /**
     * Constructor of the exception
     * @param message String Message of the error.
     */
    public CarNotFoundException(String message) {
        super(message);
    }
}
