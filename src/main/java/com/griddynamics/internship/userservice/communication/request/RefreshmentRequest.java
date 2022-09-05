package com.griddynamics.internship.userservice.communication.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class RefreshmentRequest {
    @NonNull private String refreshToken;
}
