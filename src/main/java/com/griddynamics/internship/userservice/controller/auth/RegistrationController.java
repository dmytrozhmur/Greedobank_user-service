package com.griddynamics.internship.userservice.controller.auth;

import com.griddynamics.internship.userservice.exception.EmailExistsException;
import com.griddynamics.internship.userservice.communication.request.SignupRequest;
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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

import static com.griddynamics.internship.userservice.utils.ResponseMessages.*;
import static java.util.stream.Collectors.groupingBy;

@Controller
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
            @ApiResponse(responseCode = "409", description = "Specified used email",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = JsonResponse.class)))
    })
    public ResponseEntity<JsonResponse<String>> registerUser(
            @RequestBody @Valid SignupRequest signupRequest) {
        userService.createUser(signupRequest);
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
