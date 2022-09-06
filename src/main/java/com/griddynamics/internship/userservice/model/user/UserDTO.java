package com.griddynamics.internship.userservice.model.user;

import com.griddynamics.internship.userservice.model.role.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
@RequiredArgsConstructor
@Schema(name = "user", description = "user data")
public class UserDTO {
    @NonNull private int id;
    @Size(min = 1, max = 45) @EqualsAndHashCode.Exclude private String firstName;
    @Size(min = 1, max = 45) @EqualsAndHashCode.Exclude private String lastName;
    @Size(max = 60) @Email @NonNull private String email;
    @NonNull private Role role;

    public UserDTO(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.role = user.getRole();
    }
}
