package com.griddynamics.internship.userservice.service;

import com.griddynamics.internship.userservice.controller.exception.EmailExistsException;
import com.griddynamics.internship.userservice.controller.exception.InvalidFieldException;
import com.griddynamics.internship.userservice.controller.request.SignupRequest;
import com.griddynamics.internship.userservice.model.User;
import com.griddynamics.internship.userservice.model.UserDTO;
import com.griddynamics.internship.userservice.model.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

import static com.griddynamics.internship.userservice.utils.FieldValidation.*;
import static com.griddynamics.internship.userservice.utils.Response.*;

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
        checkRequest(signup);
        signup.setPassword(passwordEncoder.encode(signup.getPassword()));

        User newUser = new User(signup);
        userRepository.save(newUser);
    }

    private void checkRequest(SignupRequest signup) {
        String email = signup.getEmail();
        if(userRepository.findByEmail(email) != null) {
            throw new EmailExistsException(EMAIL_IN_USE);
        }

        if(!email.matches(EMAIL_PATTERN)) {
            throw new InvalidFieldException(INCORRECT_EMAIL_FORMAT);
        }

        int passwordLength = signup.getPassword().length();
        if(passwordLength < PASSWORD_MIN_LENGTH || PASSWORD_MAX_LENGTH < passwordLength) {
            throw new InvalidFieldException(INVALID_PASSWORD_LENGTH);
        }
    }
}
