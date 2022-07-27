package com.griddynamics.internship.userservice.communication.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.*;

import static com.griddynamics.internship.userservice.utils.ResponseMessages.*;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class SignupRequest {
    private static final String EMAIL_PATTERN
            = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

    @NotBlank(message = EMPTY_FIELD)
    @Size(max = 45, message = EXCEEDED_SIZE)
    private final String firstName;

    @NotBlank(message = EMPTY_FIELD)
    @Size(max = 45, message = EXCEEDED_SIZE)
    private final String lastName;

    @NotBlank(message = EMPTY_FIELD)
    @Size(max = 45, message = EXCEEDED_SIZE)
    @Email(regexp = EMAIL_PATTERN, message = INCORRECT_FORMAT)
    private final String email;

    @Size(min = 8, max = 20, message = INVALID_PASSWORD_LENGTH)
    private String password;
}
