package com.griddynamics.internship.userservice.controller.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

public class MessageResponse {
    public final String message;
    public MessageResponse(String message) {
        this.message = message;
    }
}
