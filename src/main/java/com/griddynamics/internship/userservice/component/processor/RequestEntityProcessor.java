package com.griddynamics.internship.userservice.component.processor;

import com.griddynamics.internship.userservice.model.user.UserWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class RequestEntityProcessor {
    @Autowired
    private JwtProcessor jwtProcessor;

    public HttpEntity<String> generateEntityForCurrUserAuthorization() {
        HttpHeaders headers = new HttpHeaders();
        UserWrapper authUser = (UserWrapper) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        String token = jwtProcessor.generateToken(authUser);
        headers.setBearerAuth(token);

        return new HttpEntity<>(headers);
    }
}
