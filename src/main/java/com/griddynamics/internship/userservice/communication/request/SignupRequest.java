package com.griddynamics.internship.userservice.communication.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.*;

import static com.griddynamics.internship.userservice.utils.ResponseMessages.*;

@Data
@AllArgsConstructor
public class SignupRequest {
    private static final String EMAIL_PATTERN
            = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

    @NotBlank(message = EMPTY_FIELD)
    @Size(max = 45, message = EXCEEDED_SIZE)
    private String firstName;

    @NotBlank(message = EMPTY_FIELD)
    @Size(max = 45, message = EXCEEDED_SIZE)
    private String lastName;

    @NotBlank(message = EMPTY_FIELD)
    @Size(max = 45, message = EXCEEDED_SIZE)
    @Email(regexp = EMAIL_PATTERN, message = INCORRECT_FORMAT)
    private String email;

    @Size(min = 8, max = 20, message = INVALID_PASSWORD_LENGTH)
    private String password;
}
