package com.griddynamics.internship.userservice.controller;

import com.griddynamics.internship.userservice.controller.exception.EmailExistsException;
import com.griddynamics.internship.userservice.controller.exception.InvalidFieldException;
import com.griddynamics.internship.userservice.controller.request.SignupRequest;
import com.griddynamics.internship.userservice.controller.response.ErrorResponse;
import com.griddynamics.internship.userservice.controller.response.MessageResponse;
import com.griddynamics.internship.userservice.service.UserService;
import com.mysql.cj.jdbc.exceptions.MysqlDataTruncation;
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

import java.sql.SQLException;

import static com.griddynamics.internship.userservice.utils.Response.SUCCESS;

@Controller
public class RegistrationController {
    @Autowired
    private UserService userService;

    @PostMapping("/api/v1/signup")
    @Operation(summary = "Register user")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User created",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid field format",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "409", description = "Specified used email",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageResponse.class)))
    })
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signupRequest) {
        userService.registerUser(signupRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new MessageResponse(SUCCESS));
    }

    @ExceptionHandler(EmailExistsException.class)
    public ResponseEntity<?> emailRepetitionError(EmailExistsException exception) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new MessageResponse(exception.getMessage()));
    }

    @ExceptionHandler({InvalidFieldException.class, SQLException.class})
    public ResponseEntity<?> fieldFormatError(RuntimeException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(exception.getMessage()));
    }
}
