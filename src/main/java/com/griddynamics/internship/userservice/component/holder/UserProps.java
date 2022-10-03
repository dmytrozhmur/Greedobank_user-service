package com.griddynamics.internship.userservice.component.holder;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "user-service.users")
public class UserProps {
    private int pageSize = 1;
    private int startPage = 0;
}
