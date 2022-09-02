package com.griddynamics.internship.userservice.controller.auth;

import com.griddynamics.internship.userservice.communication.validation.OnPost;
import com.griddynamics.internship.userservice.communication.validation.OnUpsert;
import com.griddynamics.internship.userservice.exception.EmailExistsException;
import com.griddynamics.internship.userservice.communication.request.UserDataRequest;
import com.griddynamics.internship.userservice.communication.response.JsonResponse;
import com.griddynamics.internship.userservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.*;

import static com.griddynamics.internship.userservice.utils.ResponseMessages.*;

@Validated
@RestController
public class RegistrationController {
    @Autowired
    private UserService userService;

    @PostMapping("/api/v1/signup")
    @Operation(summary = "Register user")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User created",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = JsonResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid field format",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = JsonResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unknown sender",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Access denied",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "409", description = "Specified used email",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = JsonResponse.class)))
    })
    @Validated({OnUpsert.class, OnPost.class})
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<JsonResponse<String>> registerUser(
            @RequestBody @Valid UserDataRequest userDataRequest) {
        userService.createUser(userDataRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new JsonResponse<>(SUCCESS));
    }

    @ExceptionHandler(EmailExistsException.class)
    public ResponseEntity<JsonResponse<String>> emailRepetitionError(
            EmailExistsException exception) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new JsonResponse<>(FAILURE, Collections.singletonMap(
                        "email", new String[]{exception.getMessage()}
                )));
    }
}
