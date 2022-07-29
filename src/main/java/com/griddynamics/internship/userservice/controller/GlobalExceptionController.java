package com.griddynamics.internship.userservice.controller;

import com.griddynamics.internship.userservice.communication.response.JsonResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.*;

import static com.griddynamics.internship.userservice.utils.ResponseMessages.*;
import static java.util.stream.Collectors.groupingBy;

@ControllerAdvice
public class GlobalExceptionController {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<JsonResponse<String>> notValidFieldError(MethodArgumentNotValidException exception) {
        Map<String, String[]> errors = new LinkedHashMap<>();
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();

        fieldErrors.stream()
                .collect(groupingBy(FieldError::getField))
                .forEach((name, list) -> errors.put(name, list.stream()
                        .map(FieldError::getDefaultMessage)
                        .toArray(String[]::new)));

        return ResponseEntity
                .badRequest()
                .body(new JsonResponse<>(FAILURE, errors));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<JsonResponse<String>> bodyMissedError(HttpMessageNotReadableException exception) {
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(new JsonResponse<>(INVALID_BODY));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<JsonResponse<String>> unknownError(Exception exception) {
        return ResponseEntity
                .internalServerError()
                .body(new JsonResponse<>(UNEXPECTED, Collections.singletonMap(
                        exception.getClass().getSimpleName().toLowerCase(),
                        exception.getMessage().split(";")
                )));
    }
}
