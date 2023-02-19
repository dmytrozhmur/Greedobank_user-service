package com.griddynamics.internship.userservice.controller;

import com.griddynamics.internship.userservice.communication.request.UserDataRequest;
import com.griddynamics.internship.userservice.communication.response.JsonResponse;
import com.griddynamics.internship.userservice.communication.response.UserPage;
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
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.Optional;

import static com.griddynamics.internship.userservice.utils.PageRequests.DEFAULT_PAGE_NUM;
import static com.griddynamics.internship.userservice.utils.PageRequests.DEFAULT_PAGE_SIZE;
import static com.griddynamics.internship.userservice.utils.ResponseMessages.REGISTERED;

@Validated
@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all users")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Get user list",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unknown sender",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Access denied",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(mediaType = "application/json"))
    })
    @PreAuthorize("hasRole('ADMIN') or #email.present")
    public Page<UserDTO> getUserList(@RequestParam(defaultValue = DEFAULT_PAGE_NUM) @Min(1) int page,
                                     @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) @Min(1) int size,
                                     @RequestParam Optional<String> email) {
        UserPage users = email.isEmpty()
                ? userService.findAll(page, size)
                : userService.findAll(page, size, email.get());
        return users;
    }

    @GetMapping("/users/{id}")
    @ResponseStatus(HttpStatus.OK)
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
    public JsonResponse<UserDTO> getUserInfo(@AuthenticationPrincipal UserWrapper authUser,
                                             @PathVariable("id") int id) {
        UserDTO user = userService.findUser(id);
        return new JsonResponse<>(user);
    }

    @PatchMapping("/users/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update user by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Get user",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "401", description = "Unknown sender",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Access denied",
                    content = @Content(mediaType = "application/json"))
    })
    @Validated(OnUpsert.class)
    @PreAuthorize("isAuthenticated() and (hasRole('ADMIN') or #authUser.id == #id)")
    public JsonResponse<String> updateAccount(@AuthenticationPrincipal UserWrapper authUser,
                                              @RequestBody @Valid UserDataRequest userDataRequest,
                                              @PathVariable("id") int id) {
        userService.updateUser(id, userDataRequest);
        return new JsonResponse<>("Account has been updated");
    }

    @DeleteMapping("/users/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Delete user by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Get user",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "401", description = "Unknown sender",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Access denied",
                    content = @Content(mediaType = "application/json"))
    })
    @PreAuthorize("hasRole('ADMIN')")
    public JsonResponse<String> terminateAccount(@PathVariable("id") int id) {
        userService.deleteUser(id);
        return new JsonResponse<>("Account has been deleted");
    }
}
