package com.griddynamics.internship.userservice.controller.auth;

import com.griddynamics.internship.userservice.communication.request.ChildrenDataRequest;
import com.griddynamics.internship.userservice.communication.validation.OnPost;
import com.griddynamics.internship.userservice.communication.validation.OnUpsert;
import com.griddynamics.internship.userservice.communication.request.UserDataRequest;
import com.griddynamics.internship.userservice.communication.response.JsonResponse;
import com.griddynamics.internship.userservice.service.ChildrenService;
import com.griddynamics.internship.userservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.griddynamics.internship.userservice.utils.ResponseMessages.*;

@Slf4j
@Validated
@RestController
public class RegistrationController {
    @Autowired
    private UserService userService;
    @Autowired
    private ChildrenService childrenService;

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Register user")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User created",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Invalid field format",
                    content = @Content(mediaType = "application/json")),
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
    public JsonResponse<String> registerUser(
            @RequestBody @Valid UserDataRequest userDataRequest) {
        userService.createUser(userDataRequest);
        return new JsonResponse<>(REGISTERED);
    }

    @PostMapping("/users/{id}/createChild")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Register child account for user by id")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Child account successfully created",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unknown sender",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Access denied",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(mediaType = "application/json"))
    })
    @PreAuthorize("hasRole('ADMIN')")
    public JsonResponse<String> registerChild(@PathVariable int id,
                                              @RequestBody ChildrenDataRequest childrenRequest) {
        log.info("Received child creation request: {}", childrenRequest);
        childrenService.createChild(id, childrenRequest);
        return new JsonResponse<>(REGISTERED);
    }
}
