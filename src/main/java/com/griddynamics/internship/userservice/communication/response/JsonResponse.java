package com.griddynamics.internship.userservice.communication.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonInclude.Include;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class JsonResponse<T> {
    @NonNull private T content;

    @JsonInclude(Include.NON_NULL) private Map<String, String[]> errors;
}
