package com.griddynamics.internship.userservice.communication.request;

import com.griddynamics.internship.userservice.communication.validation.OnPost;
import com.griddynamics.internship.userservice.communication.validation.OnUpsert;
import com.griddynamics.internship.userservice.model.role.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static com.griddynamics.internship.userservice.utils.ResponseMessages.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class UserDataRequest {
    private static final String EMAIL_PATTERN
            = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

    @NotBlank(message = EMPTY_FIELD, groups = OnPost.class)
    @Size(max = 45, message = INAPPROPRIATE_SIZE, groups = OnUpsert.class)
    @NonNull private String firstName;

    @NotBlank(message = EMPTY_FIELD, groups = OnPost.class)
    @Size(max = 45, message = INAPPROPRIATE_SIZE, groups = OnUpsert.class)
    @NonNull private String lastName;

    @NotBlank(message = EMPTY_FIELD, groups = OnPost.class)
    @Size(max = 45, message = INAPPROPRIATE_SIZE, groups = OnUpsert.class)
    @Email(regexp = EMAIL_PATTERN, message = INCORRECT_FORMAT, groups = OnUpsert.class)
    @NonNull private String email;

    @NotBlank(message = EMPTY_FIELD, groups = OnPost.class)
    @Size(min = 8, max = 20, message = INVALID_PASSWORD_LENGTH, groups = OnUpsert.class)
    @NonNull private String password;

    private Role role;
}
