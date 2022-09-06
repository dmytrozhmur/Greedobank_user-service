package com.griddynamics.internship.userservice.controller;

import com.griddynamics.internship.userservice.communication.response.JsonResponse;
import com.griddynamics.internship.userservice.exception.EmailExistsException;
import com.griddynamics.internship.userservice.exception.NonExistentDataException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.util.Collection;
import java.util.Collections;
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
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public JsonResponse<String> notValidFieldError(ConstraintViolationException exception) {
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

        return new JsonResponse<>(FAILURE, errors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public JsonResponse<String> bodyMissedError() {
        return new JsonResponse<>(INVALID_BODY);
    }

    @ExceptionHandler(NonExistentDataException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public JsonResponse<String> lackingDataError(NonExistentDataException exception) {
        return new JsonResponse<>(exception.getMessage());
    }

    @ExceptionHandler(EmailExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public JsonResponse<String> emailRepetitionError(EmailExistsException exception) {
        return new JsonResponse<>(
                FAILURE,
                Collections.singletonMap(
                        "email",
                        new String[]{exception.getMessage()}
                ));
    }
}
