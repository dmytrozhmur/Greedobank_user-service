package com.griddynamics.internship.userservice.utils;

import com.griddynamics.internship.userservice.model.user.UserWrapper;
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
            = Integer.parseInt(getProperty("token.expiration"));
    private static final SecretKey SECRET
            = generateSecretKey(getProperty("token.keyword"));

    public static String generateToken(UserWrapper user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .setExpiration(new Date(new Date().getTime() + EXPIRATION))
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
    }

    public static String getEmail(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody().getSubject();
    }

    public static SecretKey generateSecretKey(String keyword) {
        byte[] decodedKey = Base64.getDecoder().decode(keyword);
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
    }
}
