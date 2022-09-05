package com.griddynamics.internship.userservice.model.user;

import lombok.Data;
import lombok.NonNull;

@Data
public class JwtUser {
    @NonNull private String accessToken;
    @NonNull private String refreshToken;
    private String type = "Bearer";
    @NonNull private Long id;
    @NonNull private String email;
    @NonNull private String username;
    @NonNull private String role;
}
