package com.griddynamics.internship.userservice.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Encryptor {

    private static BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public static String encrypt(String rawPassword) {
        return encoder.encode(rawPassword);
    }
}
