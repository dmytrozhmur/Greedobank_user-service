package com.griddynamics.internship.userservice.controller.auth;

import com.griddynamics.internship.userservice.communication.request.SigninRequest;
import com.griddynamics.internship.userservice.communication.response.JsonResponse;
import com.griddynamics.internship.userservice.exception.AuthenticationException;
import com.griddynamics.internship.userservice.model.JwtUser;
import com.griddynamics.internship.userservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@Controller
public class AuthenticationController {
    @Autowired
    private UserService userService;

    @PostMapping("/api/v1/signin")
    @Operation(summary = "Authenticate user")
    public ResponseEntity<JsonResponse<JwtUser>> authenticateUser(
            @RequestBody @Valid SigninRequest signinRequest) {
        JwtUser jwtUser = userService.getJwtResponse(signinRequest);
        return ResponseEntity.ok(new JsonResponse<>(jwtUser));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<JsonResponse<String>> authenticatingError(AuthenticationException exception) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new JsonResponse<>(exception.getMessage()));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<JsonResponse<String>> invalidCredentialsError(BadCredentialsException exception) {
        return ResponseEntity
                .unprocessableEntity()
                .body(new JsonResponse<>(exception.getMessage()));
    }
}
