package com.griddynamics.internship.userservice.controller;

import com.griddynamics.internship.userservice.communication.response.ChildrenPage;
import com.griddynamics.internship.userservice.communication.response.JsonResponse;
import com.griddynamics.internship.userservice.component.AccessCheckHelper;
import com.griddynamics.internship.userservice.exception.NonExistentDataException;
import com.griddynamics.internship.userservice.model.child.ChildDTO;
import com.griddynamics.internship.userservice.model.user.UserDTO;
import com.griddynamics.internship.userservice.model.user.UserWrapper;
import com.griddynamics.internship.userservice.service.ChildrenService;
import com.griddynamics.internship.userservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.griddynamics.internship.userservice.utils.PageRequests.DEFAULT_PAGE_NUM;
import static com.griddynamics.internship.userservice.utils.PageRequests.DEFAULT_PAGE_SIZE;

@RestController
public class ChildrenController {
    @Autowired
    private ChildrenService childrenService;

    @GetMapping("/children")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get children by parents ids")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Get children",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ChildDTO.class))),
            @ApiResponse(responseCode = "204", description = "No children for specified users",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unknown sender",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Access denied",
                    content = @Content(mediaType = "application/json"))
    })
    @PreAuthorize("hasRole('ADMIN')")
    public ChildrenPage getChildren(@RequestParam(name = "parentId", defaultValue = StringUtils.EMPTY) int[] userIds,
                                    @RequestParam(defaultValue = DEFAULT_PAGE_NUM) int page,
                                    @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int size) {
        return childrenService.findChildren(userIds, page, size);
    }

    @GetMapping("/children/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get child by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Child got",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unknown sender",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Access denied",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Child not found",
                    content = @Content(mediaType = "application/json"))
    })
    @PreAuthorize("hasRole('ADMIN') or @accessCheck.hasChildAccountAccess(#authUser, #id)")
    public JsonResponse<ChildDTO> getChild(@AuthenticationPrincipal UserWrapper authUser,
                                             @PathVariable("id") int id) {
        ChildDTO child = childrenService.findChild(id);
        return new JsonResponse<>(child);
    }

    @PostMapping("/children/{childId}/link/{parentId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Link child by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Child got",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unknown sender",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Access denied",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Child not found",
                    content = @Content(mediaType = "application/json"))
    })
    @PreAuthorize("hasRole('ADMIN')")
    public JsonResponse<ChildDTO> linkChildWithParent(@AuthenticationPrincipal UserWrapper authUser,
                                                      @PathVariable("childId") int id) {
        ChildDTO child = childrenService.findChild(id);
        return new JsonResponse<>(child);
    }
}
