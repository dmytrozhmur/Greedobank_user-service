package com.griddynamics.internship.userservice.utils;

import com.griddynamics.internship.userservice.model.UserDetailsImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.Authentication;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;

public class JwtUtils {
    private static final int EXPIRATION_MILLI = 100000000;

    private static String secret = "secret";

    public static String generateToken(Authentication auth) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) auth.getPrincipal();

        return Jwts.builder()
                .setSubject(userPrincipal.getEmail())
                .setExpiration(new Date(new Date().getTime() + EXPIRATION_MILLI))
                .signWith(SignatureAlgorithm.HS256, generateSecretKey(secret))
                .compact();
    }

    public static String getEmail(String token) {
        return Jwts.parser()
                .setSigningKey(generateSecretKey(secret))
                .parseClaimsJwt(token)
                .getBody().getSubject();
    }

    public static SecretKey generateSecretKey(String secretWord) {
        byte[] decodedKey = Base64.getDecoder().decode(secretWord);
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
    }
}
