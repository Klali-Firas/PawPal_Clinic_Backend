package com.PawPal_Clinic.Backend.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtException;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtility {

    private final Key secretKey;
    private final long expiration;

    public JwtUtility(@Value("${spring.jwt.secret}") String secret,
                      @Value("${spring.jwt.expiration}") long expiration) {
        // Pad the secret key to ensure it is at least 64 bytes long
        String paddedSecret = String.format("%-64s", secret).substring(0, 64);
        this.secretKey = new SecretKeySpec(paddedSecret.getBytes(StandardCharsets.UTF_8),
                Keys.hmacShaKeyFor(paddedSecret.getBytes(StandardCharsets.UTF_8)).getAlgorithm());
        this.expiration = expiration;
    }

    public String generateToken(String email, String role) {
        return Jwts.builder().subject(email)
                .claim("role", role).issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .signWith((SecretKey) secretKey, Jwts.SIG.HS512)
                .compact();
    }

    public String getEmail(String token) {
        Claims claims = parseToken(token);
        return claims.getSubject();
    }

    public String getRole(String token) {
        Claims claims = parseToken(token);
        return claims.get("role", String.class);
    }

    public void validateToken(String token) throws JwtException {
        parseToken(token);
    }

    private Claims parseToken(String token) throws JwtException {
        return Jwts.parser()
                .verifyWith((SecretKey) secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean isTokenExpired(String token) {
        return parseToken(token).getExpiration().before(new Date());
    }
}
