package com.griddynamics.internship.userservice.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @GetMapping("/api/v1/users")
    public String list() {
        return "Empty user list";
    }

}
