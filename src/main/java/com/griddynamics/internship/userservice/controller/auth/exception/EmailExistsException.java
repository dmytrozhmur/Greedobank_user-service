package com.griddynamics.internship.userservice.controller.auth.exception;

public class EmailExistsException extends RuntimeException {
    public EmailExistsException(String message) {
        super(message);
    }
}
