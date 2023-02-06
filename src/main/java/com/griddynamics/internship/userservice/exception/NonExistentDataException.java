package com.griddynamics.internship.userservice.exception;

public class NonExistentDataException extends RuntimeException {
    public NonExistentDataException(String message) {
        super(message);
    }
}
