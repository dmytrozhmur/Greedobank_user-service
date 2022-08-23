package com.griddynamics.internship.userservice.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.griddynamics.internship.userservice.communication.response.JsonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@Component
public class MissedAuthenticationPoint implements AuthenticationEntryPoint {
    public static final String HEADER_EXPIRED = "expired";
    @Autowired
    private ObjectMapper mapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        JsonResponse<String> missedAuthResponse;
        ServletOutputStream responseStream = response.getOutputStream();
        String responseMessage = authException.getMessage();

        if(request.getAttribute(HEADER_EXPIRED) != null) {
            missedAuthResponse = new JsonResponse<>(
                    responseMessage,
                    Collections.singletonMap(
                            HEADER_EXPIRED,
                            String.valueOf(request.getAttribute(HEADER_EXPIRED)).split("\\.\s*")
                    )
            );
        } else {
            missedAuthResponse = new JsonResponse<>(responseMessage);
        }

        mapper.writeValue(responseStream, missedAuthResponse);
    }
}
