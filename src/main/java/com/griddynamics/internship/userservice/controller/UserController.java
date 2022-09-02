package com.griddynamics.internship.userservice.controller;

import com.griddynamics.internship.userservice.communication.request.UserDataRequest;
import com.griddynamics.internship.userservice.communication.response.JsonResponse;
import com.griddynamics.internship.userservice.communication.validation.OnUpsert;
import com.griddynamics.internship.userservice.model.user.UserDTO;
import com.griddynamics.internship.userservice.model.user.UserWrapper;
import com.griddynamics.internship.userservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
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
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Access denied",
                    content = @Content(mediaType = "application/json")),
    })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Collection<UserDTO>> getUserList() {
        Collection<UserDTO> users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/api/v1/users/{id}")
    @Operation(summary = "Get user by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Get user",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unknown sender",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Access denied",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(mediaType = "application/json"))
    })
    @PreAuthorize("isAuthenticated() and (hasRole('ADMIN') or #authUser.id == #id)")
    public ResponseEntity<UserDTO> getUserInfo(@AuthenticationPrincipal UserWrapper authUser,
                                               @PathVariable("id") int id) {
        UserDTO user = userService.findUser(id);
        return ResponseEntity.ok(user);
    }

    @PatchMapping("/api/v1/users/{id}")
    @Operation(summary = "Update user by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Get user",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unknown sender",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Access denied",
                    content = @Content(mediaType = "application/json"))
    })
    @Validated(OnUpsert.class)
    @PreAuthorize("isAuthenticated() and (hasRole('ADMIN') or #authUser.id == #id)")
    public ResponseEntity<JsonResponse<String>> updateAccount(@AuthenticationPrincipal UserWrapper authUser,
                                                              @RequestBody @Valid UserDataRequest userDataRequest,
                                                              @PathVariable("id") int id) {
        userService.updateUser(id, userDataRequest);
        return ResponseEntity.ok(new JsonResponse<>("Account has been updated"));
    }

    @DeleteMapping("/api/v1/users/{id}")
    @Operation(summary = "Delete user by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Get user",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unknown sender",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Access denied",
                    content = @Content(mediaType = "application/json"))
    })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<JsonResponse<String>> terminateAccount(@PathVariable("id") int id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(new JsonResponse<>("Account has been deleted"));
    }
}
