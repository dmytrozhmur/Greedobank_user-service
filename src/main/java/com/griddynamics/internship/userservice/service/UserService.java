package com.griddynamics.internship.userservice.service;

import com.griddynamics.internship.userservice.model.User;
import com.griddynamics.internship.userservice.model.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public Iterable<User> findAll() {
        return userRepository.findAll();
    }
}
