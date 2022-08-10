package com.griddynamics.internship.userservice.exception;

public class NonAvailableDataException extends RuntimeException {
    public NonAvailableDataException(String message) {
        super(message);
    }
}
