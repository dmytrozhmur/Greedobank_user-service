package com.griddynamics.internship.userservice.controller.auth.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonInclude.Include;

@AllArgsConstructor
@RequiredArgsConstructor
public class JsonResponse {
    public final String message;

    @Getter
    @JsonInclude(Include.NON_NULL) private Map<String, String[]> errors;
}
