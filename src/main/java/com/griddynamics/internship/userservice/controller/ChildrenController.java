package com.griddynamics.internship.userservice.controller;

import com.griddynamics.internship.userservice.communication.response.ChildrenPage;
import com.griddynamics.internship.userservice.model.child.ChildDTO;
import com.griddynamics.internship.userservice.service.ChildrenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static com.griddynamics.internship.userservice.utils.PageRequests.DEFAULT_PAGE_NUM;
import static com.griddynamics.internship.userservice.utils.PageRequests.DEFAULT_PAGE_SIZE;

@RestController("/api/v1/children")
public class ChildrenController {
    @Autowired
    private ChildrenService childrenService;

    @GetMapping
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
    public ChildrenPage getChildren(@RequestParam(name = "userId") int[] userIds,
                                    @RequestParam(defaultValue = DEFAULT_PAGE_NUM) int page,
                                    @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int size) {
        return childrenService.findChildren(userIds, page, size);
    }
}
