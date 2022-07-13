package com.griddynamics.internship.userservice.controller;

import com.griddynamics.internship.userservice.model.UserDTO;
import com.griddynamics.internship.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/api/v1/users")
    public Iterable<UserDTO> getUserList() {
        return userService.findAll();
    }

}
