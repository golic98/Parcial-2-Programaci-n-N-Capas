package com.example.P2PNC.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

@Component

public class JwtUtils {
    private final SecretKey key;
    private static final long EXPIRATION_TIME = 86400000L;

    @Autowired
    public JwtUtils(SecretKey key) {
        this.key = key;
    }

    public String generateToken(String username, String role) {
        HashMap<String, Object> claims = new HashMap<>();
        claims.put("role", role);

        return Jwts.builder()
                .claims(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key)
                .compact();
    }

    public  String generateRefreshToken(HashMap<String, Object> claims, String username) {
        return Jwts.builder()
                .claims(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key)
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    public String extractRole(String token) {
        return extractClaims(token, claims -> claims.get("role", String.class));
    }

    private <T> T extractClaims(String token, Function<Claims, T> claimsTFunction) {
        return claimsTFunction.apply(Jwts.parser()
                .verifyWith(key).build()
                .parseSignedClaims(token)
                .getPayload());
    }

    public  boolean isTokenValid(String token, String username) {
        final String auxUsername = extractUsername(token);
        return (auxUsername.equals(username) && !isTokenExpired(token));
    }

    public  boolean isTokenExpired(String token) {
        return extractClaims(token, Claims::getExpiration).before(new Date());
    }
}
