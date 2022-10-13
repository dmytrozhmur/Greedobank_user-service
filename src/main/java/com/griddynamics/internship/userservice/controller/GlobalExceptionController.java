package com.griddynamics.internship.userservice.controller;

import com.griddynamics.internship.userservice.communication.response.JsonResponse;
import com.griddynamics.internship.userservice.exception.EmailExistsException;
import com.griddynamics.internship.userservice.exception.NonExistentDataException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import static com.griddynamics.internship.userservice.utils.ResponseMessages.FAILURE;
import static com.griddynamics.internship.userservice.utils.ResponseMessages.INVALID_BODY;
import static com.griddynamics.internship.userservice.utils.ResponseMessages.INVALID_URL_PARAMS;
import static java.util.stream.Collectors.groupingBy;

@Slf4j
@ResponseBody
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
    public JsonResponse<String> bodyMissedError(HttpMessageNotReadableException e) {
        log.error("Handled exception", e);
        return new JsonResponse<>(INVALID_BODY);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public JsonResponse<String> urlParamMissedError() {
        return new JsonResponse<>(INVALID_URL_PARAMS);
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
