package com.griddynamics.internship.userservice.model;

import lombok.Data;
import lombok.NonNull;

@Data
public class JwtUser {
    @NonNull private String token;
    @NonNull private long id;
    @NonNull private String email;
}
