package com.griddynamics.internship.userservice.communication.request;

import com.griddynamics.internship.userservice.communication.validation.OnPost;
import com.griddynamics.internship.userservice.communication.validation.OnUpsert;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static com.griddynamics.internship.userservice.utils.ResponseMessages.*;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class ChildrenDataRequest {
    @NotNull(message = EMPTY_FIELD, groups = OnPost.class)
    @Size(min = 6, max = 30, message = INAPPROPRIATE_SIZE, groups = OnUpsert.class)
    @NonNull private String login;

    @NotBlank(message = EMPTY_FIELD, groups = OnPost.class)
    @Size(min = 8, max = 20, message = INVALID_PASSWORD_LENGTH, groups = OnUpsert.class)
    @NonNull private String password;

    @Size(min = 8, max = 20, message = INVALID_PASSWORD_LENGTH, groups = OnUpsert.class)
    private String contactDetails;
}
