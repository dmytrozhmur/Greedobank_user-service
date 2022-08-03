package com.griddynamics.internship.userservice.utils;

import com.griddynamics.internship.userservice.model.UserWrapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.Authentication;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;

import static com.griddynamics.internship.userservice.utils.PropertiesUtils.getProperty;

public class JwtUtils {
    private static final int EXPIRATION
            = Integer.parseInt(getProperty("secret.expiration"));
    private static final String SECRET = getProperty("secret.keyword");

    public static String generateToken(Authentication auth) {
        UserWrapper userPrincipal = (UserWrapper) auth.getPrincipal();

        return Jwts.builder()
                .setSubject(userPrincipal.getEmail())
                .setExpiration(new Date(new Date().getTime() + EXPIRATION))
                .signWith(SignatureAlgorithm.HS256, generateSecretKey())
                .compact();
    }

    public static SecretKey generateSecretKey() {
        byte[] decodedKey = Base64.getDecoder().decode(SECRET);
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
    }
}
