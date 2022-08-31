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
        //List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();

        violations.stream()
                .collect(groupingBy(ConstraintViolation::getPropertyPath))
                .forEach((key, value) -> {
                    Stream keyStream = ((Collection) key).stream();
                    errors.put(
                        keyStream.skip(keyStream.count() - 1).findFirst().get().toString(),
                        value.stream()
                                    .map(ConstraintViolation::getMessage)
                                    .toArray(String[]::new)
                    );
                });

//                .forEach((path, list) -> errors.put(
//                        path,
//                        list.stream()
//                                .map(ConstraintViolation::getMessage)
//                                .toArray(String[]::new)));

//        fieldErrors.stream()
//                .collect(groupingBy(FieldError::getField))
//                .forEach((name, list) -> errors.put(name, list.stream()
//                        .map(FieldError::getDefaultMessage)
//                        .toArray(String[]::new)));

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
