package com.griddynamics.internship.userservice.controller.auth;

import com.griddynamics.internship.userservice.communication.request.SigninRequest;
import com.griddynamics.internship.userservice.communication.response.JsonResponse;
import com.griddynamics.internship.userservice.model.JwtUser;
import com.griddynamics.internship.userservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

import static com.griddynamics.internship.userservice.utils.ResponseMessages.SUCCESS;

@Controller
public class AuthenticationController {
    @Autowired
    private UserService userService;

    @PostMapping("/api/v1/signin")
    @Operation(summary = "Authenticate user")
    public ResponseEntity<JsonResponse<JwtUser>> authenticateUser(
            @RequestBody @Valid SigninRequest signinRequest) {
        JwtUser jwt = userService.getJwtResponse(signinRequest);
        return ResponseEntity.ok(new JsonResponse<>(jwt));
    }
}
