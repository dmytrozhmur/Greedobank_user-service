package com.griddynamics.internship.userservice.controller;

import com.griddynamics.internship.userservice.model.User;
import com.griddynamics.internship.userservice.model.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/api/v1/users")
    public @ResponseBody Iterable<User> getUserList() {
        return userRepository.findAll();
    }

}
