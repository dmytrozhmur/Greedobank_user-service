package com.griddynamics.internship.userservice.controller.response;

public class ErrorResponse {
    public final String error;

    public ErrorResponse(String error) {
        int lastIndex = error.indexOf(';');
        this.error = error.substring(0, (lastIndex == -1 ? error.length() : lastIndex));
    }
}
