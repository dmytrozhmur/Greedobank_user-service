package com.griddynamics.internship.userservice.controller;

import com.griddynamics.internship.userservice.communication.response.JsonResponse;
import com.griddynamics.internship.userservice.exception.NonExistentDataException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

import static com.griddynamics.internship.userservice.utils.ResponseMessages.*;
import static java.util.stream.Collectors.groupingBy;

@ControllerAdvice
public class GlobalExceptionController {
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<JsonResponse<String>> notValidFieldError(ConstraintViolationException exception) {
        Map<String, String[]> errors = new LinkedHashMap<>();
        Set<ConstraintViolation<?>> violations = exception.getConstraintViolations();

        violations.stream()
                .collect(groupingBy(ConstraintViolation::getPropertyPath))
                .forEach((key, value) -> {
                    Path.Node field = null;
                    for (Iterator<Path.Node> iterator = key.iterator(); iterator.hasNext(); field = iterator.next()) {}
                    errors.put(
                        field.getName(),
                        value.stream()
                                    .map(ConstraintViolation::getMessage)
                                    .toArray(String[]::new)
                    );
                });

        return ResponseEntity
                .badRequest()
                .body(new JsonResponse<>(FAILURE, errors));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<JsonResponse<String>> bodyMissedError() {
        return ResponseEntity
                .unprocessableEntity()
                .body(new JsonResponse<>(INVALID_BODY));
    }

    @ExceptionHandler(NonExistentDataException.class)
    public ResponseEntity<JsonResponse<String>> lackingDataError(NonExistentDataException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new JsonResponse<>(exception.getMessage()));
    }
}
