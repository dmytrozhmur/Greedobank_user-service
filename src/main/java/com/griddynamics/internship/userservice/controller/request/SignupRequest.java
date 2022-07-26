package com.griddynamics.internship.userservice.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
public class SignupRequest {
    @Getter @Setter
    @NotBlank @Size(min = 1, max = 45)
    private String firstName;

    @Getter @Setter
    @NotBlank @Size(min = 1, max = 45)
    private String lastName;

    @Getter @Setter
    @NotBlank @Size(max = 60) @Email
    private String email;

    @Getter @Setter
    @NotBlank @Size(min = 8, max = 40)
    private String password;
}
