package com.griddynamics.internship.userservice.controller;

import com.griddynamics.internship.userservice.communication.response.JsonResponse;
import com.griddynamics.internship.userservice.model.card.CardTemplateDTO;
import com.griddynamics.internship.userservice.model.user.UserDTO;
import com.griddynamics.internship.userservice.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;

@Validated
@RestController
@RequestMapping("/api/v1/admins")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all users")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Get admin list",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unknown sender",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Access denied",
                    content = @Content(mediaType = "application/json"))
    })
    @PreAuthorize("hasRole('ADMIN')")
    public Page<UserDTO> getAdminList(@RequestParam(defaultValue = "1") @Min(1) int page,
                                      @RequestParam(defaultValue = "5") @Min(1) int size) {
        return adminService.findAll(page, size);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get admin by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Get admin",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unknown sender",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Access denied",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Admin not found",
                    content = @Content(mediaType = "application/json"))
    })
    @PreAuthorize("hasRole('ADMIN')")
    public JsonResponse<UserDTO> getAdminInfo(@PathVariable("id") int id) {
        UserDTO admin = adminService.findAdmin(id);
        return new JsonResponse<>(admin);
    }

    @GetMapping("/{id}/card_templates")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get card templates by admin id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Get admin",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unknown sender",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Access denied",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Admin not found",
                    content = @Content(mediaType = "application/json"))
    })
    @PreAuthorize("hasRole('ADMIN')")
    public Page<CardTemplateDTO> getAdminTemplates(@PathVariable("id") int id) {
        return adminService.findCardTemplates(id);
    }
}
