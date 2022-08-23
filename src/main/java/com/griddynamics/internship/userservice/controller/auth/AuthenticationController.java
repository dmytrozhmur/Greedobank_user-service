package com.griddynamics.internship.userservice.controller.auth;

import com.griddynamics.internship.userservice.communication.request.RefreshmentRequest;
import com.griddynamics.internship.userservice.communication.request.SigninRequest;
import com.griddynamics.internship.userservice.communication.response.JsonResponse;
import com.griddynamics.internship.userservice.model.token.JwtRefreshment;
import com.griddynamics.internship.userservice.model.user.JwtUser;
import com.griddynamics.internship.userservice.service.RefreshmentService;
import com.griddynamics.internship.userservice.service.UserService;
import io.jsonwebtoken.ExpiredJwtException;
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
    @Autowired
    private RefreshmentService refreshmentService;

    @PostMapping("/api/v1/signin")
    @Operation(summary = "Authenticate user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User authenticated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = JwtUser.class))),
            @ApiResponse(responseCode = "400", description = "Invalid field(s) format",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Authentication failed",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "422", description = "Invalid body",
                    content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<JsonResponse<JwtUser>> authenticateUser(
            @RequestBody @Valid SigninRequest signinRequest) {
        JwtUser jwtUser = userService.verifyUser(signinRequest);
        return ResponseEntity.ok(new JsonResponse<>(jwtUser));
    }

    @PostMapping("/api/v1/refreshToken")
    @Operation(summary = "Get new access token")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Access token sent",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = JwtRefreshment.class))),
            @ApiResponse(responseCode = "400", description = "Invalid field(s) format",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Token for refresh not found",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "422", description = "Invalid body format",
                    content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<JsonResponse<JwtRefreshment>> refreshAuthentication(
            @RequestBody @Valid RefreshmentRequest refreshmentRequest) {
        JwtRefreshment refreshment = refreshmentService
                .updateAccessToken(refreshmentRequest.getRefreshToken());
        return ResponseEntity.ok(new JsonResponse<>(refreshment));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<JsonResponse<String>> invalidCredentialsError(BadCredentialsException exception) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new JsonResponse<>(exception.getMessage()));
    }
}
