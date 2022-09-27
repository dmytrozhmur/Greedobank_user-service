package com.griddynamics.internship.userservice.component.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.griddynamics.internship.userservice.communication.response.JsonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class UnsuitableRoleHandler implements AccessDeniedHandler {
    @Autowired
    private ObjectMapper mapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        JsonResponse<String> accessDeniedResponse =
                new JsonResponse<>(accessDeniedException.getMessage());
        mapper.writeValue(response.getOutputStream(), accessDeniedResponse);
    }
}
