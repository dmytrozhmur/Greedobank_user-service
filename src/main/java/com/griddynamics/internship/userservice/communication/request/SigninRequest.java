package com.griddynamics.internship.userservice.communication.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class SigninRequest {
    @NonNull @NotBlank private String email;
    @NonNull @NotBlank private String password;
}
