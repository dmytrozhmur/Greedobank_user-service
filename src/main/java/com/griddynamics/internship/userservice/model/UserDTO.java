package com.griddynamics.internship.userservice.model;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.*;
import java.util.Objects;

@Schema(name = "user", description = "user data")
public class UserDTO {
    @NotNull
    private int id;
    @NotBlank @Size(min = 1, max = 45)
    private String firstName;
    @NotBlank @Size(min = 1, max = 45)
    private String lastName;
    @NotBlank @Size(max = 60) @Email
    private String email;

    public UserDTO(int id, String firstName, String lastName, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public UserDTO(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
