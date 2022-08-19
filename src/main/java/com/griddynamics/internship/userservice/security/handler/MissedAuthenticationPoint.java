package com.griddynamics.internship.userservice.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.griddynamics.internship.userservice.communication.response.JsonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Locale;

@Component
public class MissedAuthenticationPoint implements AuthenticationEntryPoint {
    public static final String HEADER = "expired";
    @Autowired
    private ObjectMapper mapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        JsonResponse<String> missedAuthResponse;
        ServletOutputStream responseStream = response.getOutputStream();
        String responseMessage = authException.getMessage();

        if(request.getAttribute("expired") != null) {
            missedAuthResponse = new JsonResponse<>(
                    responseMessage,
                    Collections.singletonMap(
                            HEADER,
                            String.valueOf(request.getAttribute(HEADER)).split("\\.\s*")
                    )
            );
        } else {
            missedAuthResponse = new JsonResponse<>(responseMessage);
        }

        mapper.writeValue(responseStream, missedAuthResponse);
    }
}
