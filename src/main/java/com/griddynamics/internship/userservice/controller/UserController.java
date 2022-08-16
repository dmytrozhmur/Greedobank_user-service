package com.griddynamics.internship.userservice.controller;

import com.griddynamics.internship.userservice.communication.response.JsonResponse;
import com.griddynamics.internship.userservice.model.JwtUser;
import com.griddynamics.internship.userservice.model.User;
import com.griddynamics.internship.userservice.model.UserDTO;
import com.griddynamics.internship.userservice.model.UserWrapper;
import com.griddynamics.internship.userservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;


@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/api/v1/users")
    @Operation(summary = "Get all users")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Get user list",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unknown sender",
                    content = @Content(mediaType = "text/html")),
            @ApiResponse(responseCode = "403", description = "Access denied",
                    content = @Content(mediaType = "text/html")),
            @ApiResponse(responseCode = "404", description = "Users not found",
                    content = @Content(mediaType = "text/html"))
    })
    @PreAuthorize("hasRole('ADMIN')")
    public Collection<UserDTO> getUserList() {
        return userService.findAll();
    }

    @GetMapping("/api/v1/users/{id}")
    @Operation(summary = "Get user by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Get user",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unknown sender",
                    content = @Content(mediaType = "text/html")),
            @ApiResponse(responseCode = "403", description = "Access denied",
                    content = @Content(mediaType = "text/html")),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(mediaType = "text/html"))
    })
    @PreAuthorize("isAuthenticated() and (hasRole('ADMIN') or #authUser.id == #id)")
    public UserDTO getUser(@AuthenticationPrincipal UserWrapper authUser, @PathVariable("id") int id) {
        return userService.findUser(id);
    }
}
