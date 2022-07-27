package com.griddynamics.internship.userservice.communication.request;

import lombok.Data;
import lombok.NonNull;

@Data
public class SigninRequest {
    @NonNull private String email;
    @NonNull private String password;
}
