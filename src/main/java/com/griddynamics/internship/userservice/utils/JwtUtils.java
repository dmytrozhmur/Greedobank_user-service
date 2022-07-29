package com.griddynamics.internship.userservice.utils;

import com.griddynamics.internship.userservice.model.UserDetailsImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.Authentication;

import java.util.Date;

public class JwtUtils {
    private static final int EXPIRATION_MILLI = 100000000;

    private static String secretKey = "secret";

    public static String generateToken(Authentication auth) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) auth.getPrincipal();

        return Jwts.builder()
                .setSubject(userPrincipal.getEmail())
                .setExpiration(new Date(new Date().getTime() + EXPIRATION_MILLI))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public static String getEmail(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJwt(token).getBody().getSubject();
    }
}
