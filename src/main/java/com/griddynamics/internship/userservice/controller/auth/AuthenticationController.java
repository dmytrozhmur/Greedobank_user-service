package com.griddynamics.internship.userservice.controller.auth;

import com.griddynamics.internship.userservice.communication.request.SigninRequest;
import com.griddynamics.internship.userservice.communication.response.JsonResponse;
import com.griddynamics.internship.userservice.exception.SignInException;
import com.griddynamics.internship.userservice.model.JwtUser;
import com.griddynamics.internship.userservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class AuthenticationController {
    @Autowired
    private UserService userService;

    @PostMapping("/api/v1/signin")
    @Operation(summary = "Authenticate user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User authenticated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = JsonResponse.class))),
            @ApiResponse(responseCode = "401", description = "Authentication failed",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = JsonResponse.class))),
            @ApiResponse(responseCode = "422", description = "Invalid email or password",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = JsonResponse.class)))
    })
    public ResponseEntity<JsonResponse<JwtUser>> authenticateUser(
            @RequestBody @Valid SigninRequest signinRequest) {
        JwtUser jwtUser = userService.verifyUser(signinRequest);
        return ResponseEntity.ok(new JsonResponse<>(jwtUser));
    }

    @ExceptionHandler(SignInException.class)
    public ResponseEntity<JsonResponse<String>> authError(SignInException exception) {
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
