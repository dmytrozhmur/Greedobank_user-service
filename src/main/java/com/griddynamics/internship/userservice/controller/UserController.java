package com.griddynamics.internship.userservice.controller;

import com.griddynamics.internship.userservice.model.User;
import com.griddynamics.internship.userservice.model.UserRepository;
import com.griddynamics.internship.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/api/v1/users")
    public Iterable<User> getUserList() {
        return userService.findAll();
    }

}
