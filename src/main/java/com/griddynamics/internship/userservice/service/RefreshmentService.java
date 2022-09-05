package com.griddynamics.internship.userservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.griddynamics.internship.userservice.exception.InactiveRefreshmentException;
import com.griddynamics.internship.userservice.exception.NonExistentDataException;
import com.griddynamics.internship.userservice.model.token.JwtRefreshment;
import com.griddynamics.internship.userservice.model.token.Refreshment;
import com.griddynamics.internship.userservice.model.user.UserWrapper;
import com.griddynamics.internship.userservice.repo.RefreshmentRepository;
import com.griddynamics.internship.userservice.repo.UserRepository;
import com.griddynamics.internship.userservice.utils.JwtUtils;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.web.header.Header;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshmentService {
    @Value("${token.refresh.expiration}")
    private long expiration;
    private UserRepository userRepository;
    private RefreshmentRepository refreshmentRepository;

    @Autowired
    public RefreshmentService(UserRepository userRepository, RefreshmentRepository refreshmentRepository) {
        this.userRepository = userRepository;
        this.refreshmentRepository = refreshmentRepository;
    }

    public Refreshment generateRefreshToken(String email) {
        Refreshment refreshment = new Refreshment(
                UUID.randomUUID().toString(),
                Instant.now().plusMillis(expiration),
                userRepository.findByEmail(email)
        );
        refreshmentRepository.save(refreshment);
        return refreshment;
    }

    public JwtRefreshment updateAccessToken(String refreshToken) {
        return refreshmentRepository.findByToken(refreshToken)
                .map(RefreshmentService::verifyExpiration)
                .map(Refreshment::getUser)
                .map(UserWrapper::new)
                .map(JwtUtils::generateToken)
                .map(accessToken -> new JwtRefreshment(accessToken, refreshToken))
                .orElseThrow(() -> new NonExistentDataException("Refreshment token hasn't been found"));
    }

    private static Refreshment verifyExpiration(Refreshment refreshment) {
        if(refreshment.getExpiration().isAfter(Instant.now())) return refreshment;
        throw new InactiveRefreshmentException(
                    "Refreshment token isn't active. Please, provide authentication again.");
    }
}
