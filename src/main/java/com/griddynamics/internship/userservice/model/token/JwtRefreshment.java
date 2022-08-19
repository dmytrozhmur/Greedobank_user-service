package com.griddynamics.internship.userservice.model.token;

import lombok.Data;
import lombok.NonNull;

@Data
public class JwtRefreshment {
    @NonNull private String accessToken;
    @NonNull private String refreshToken;
    private String tokenType = "Bearer";
}
