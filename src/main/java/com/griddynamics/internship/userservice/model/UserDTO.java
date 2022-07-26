package com.griddynamics.internship.userservice.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.*;
import java.util.Objects;

@AllArgsConstructor
@Schema(name = "user", description = "user data")
public class UserDTO {
    @Getter
    @NotNull
    private int id;
    @Getter
    @NotBlank @Size(min = 1, max = 45)
    private String firstName;
    @Getter
    @NotBlank @Size(min = 1, max = 45)
    private String lastName;
    @Getter
    @NotBlank @Size(max = 60) @Email
    private String email;

    public UserDTO(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDTO userDTO = (UserDTO) o;
        return Objects.equals(firstName, userDTO.firstName) && Objects.equals(lastName, userDTO.lastName) && Objects.equals(email, userDTO.email);
    }

    @Override
    public int hashCode() {
        return 43 * (email == null ? 0 : email.length());
    }
}
