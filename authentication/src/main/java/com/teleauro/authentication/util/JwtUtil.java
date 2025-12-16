
package com.teleauro.authentication.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.teleauro.model.user.Roles;

import jakarta.annotation.PostConstruct;
import java.util.Date;
import java.util.List;
import java.security.Key;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration; // in milliseconds

    private Key key;

    @PostConstruct
    public void init() {
        // Ensure secret is long enough for HMAC SHA-256 (at least 32 bytes)
        if (secret.length() < 32) {
            throw new IllegalArgumentException("JWT secret must be at least 32 characters for HS256");
        }
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    /** Generate JWT token with expiration */
    public String generateToken(String username, List<Roles> roles) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .claim("roles", roles.stream().map(e -> e.name()).toList())
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /** Validate JWT token */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    /** Extract username from token */
    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /** Return configured expiration time in milliseconds */
    public long getExpiration() {
        return expiration;
    }
}
