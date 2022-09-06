package com.griddynamics.internship.userservice.model.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.griddynamics.internship.userservice.model.role.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Objects;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Schema(name = "user", description = "user data")
public class UserDTO {
    @NonNull private int id;
    @NotBlank
    @Size(min = 1, max = 45)
    private String firstName;
    @NotBlank @Size(min = 1, max = 45)
    private String lastName;
    @NotBlank @Size(max = 60) @Email
    @NonNull private String email;
    @JsonProperty("role")
    @NonNull private Role role;

    public UserDTO(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.role = user.getRole();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDTO userDTO = (UserDTO) o;
        return Objects.equals(firstName, userDTO.firstName)
                && Objects.equals(lastName, userDTO.lastName)
                && Objects.equals(email, userDTO.email);
    }

    @Override
    public int hashCode() {
        return 43 * (email == null ? 0 : email.length());
    }
}
