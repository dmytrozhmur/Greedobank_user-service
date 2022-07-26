package com.griddynamics.internship.userservice.service;

import com.griddynamics.internship.userservice.controller.auth.exception.EmailExistsException;
import com.griddynamics.internship.userservice.controller.auth.request.SignupRequest;
import com.griddynamics.internship.userservice.model.User;
import com.griddynamics.internship.userservice.model.UserDTO;
import com.griddynamics.internship.userservice.model.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

import static com.griddynamics.internship.userservice.utils.Responses.EMAIL_IN_USE;

@Service
public class UserService {
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public UserService(@Autowired UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Collection<UserDTO> findAll() {
        return userRepository.findAll()
                .stream()
                .map(UserDTO::new)
                .collect(Collectors.toList());
    }

    public void registerUser(SignupRequest signup) {
        if(userRepository.findByEmail(signup.getEmail()) != null)
            throw new EmailExistsException(EMAIL_IN_USE);
        signup.setPassword(passwordEncoder.encode(signup.getPassword()));

        User newUser = new User(signup);
        userRepository.save(newUser);
    }
}
