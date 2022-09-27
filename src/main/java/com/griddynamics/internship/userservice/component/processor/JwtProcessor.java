package com.griddynamics.internship.userservice.component.processor;

import com.griddynamics.internship.userservice.model.user.UserWrapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtProcessor {
    @Value("${token.expiration}")
    private int expiration;
    private SecretKey secret;

    public JwtProcessor(@Value("${token.keyword}") String keyword) {
        secret = generateSecretKey(keyword);
    }

    public String generateToken(UserWrapper user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .setExpiration(new Date(new Date().getTime() + expiration))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public String getEmail(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody().getSubject();
    }

    public SecretKey generateSecretKey(String keyword) {
        byte[] decodedKey = Base64.getDecoder().decode(keyword);
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
    }
}
