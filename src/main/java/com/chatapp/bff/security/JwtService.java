package com.chatapp.bff.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import java.nio.charset.StandardCharsets;
import java.security.Key;

@Service
public class JwtService {
    private final Key key;

    public JwtService(@Value("${security.jwt.secret}") String secret) {

        if (secret == null || secret.isBlank() || secret.length() < 32) {
            throw new IllegalArgumentException("JWT secret must be at least 32 characters");
        }

        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean isTokenValid(String token) {
        try {
            String username = extractUsername(token);
            return username != null && !username.isBlank();
        } catch (Exception e) {
            return false;
        }
    }
}
