package com.pulse.auth.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    private static final String SECRET = "pulse-secret-key-must-be-at-least-32-bytes!!"; // Will change this once deployment, move key to .env
    private static final long EXPIRATION_MS = 86400000; // For 24 gours

    /**
     * Builds the signing key from the secret string.
     *
     * @return the HMAC-SHA signing key
     */
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    /**
     * Generates a signed JWT token for the given username.
     * Token expires after 24 hours.
     *
     * @param username the authenticated user's username
     * @return signed JWT token string
     */
    public String generateToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Extracts the username from a signed JWT token.
     *
     * @param token the JWT token string
     * @return the username stored in the token's subject claim
     */
    public String extractUsername(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    /**
     * Validates a JWT token by attempting to parse it.
     * Returns false if the token is expired, malformed, or tampered with.
     *
     * @param token the JWT token string
     * @return true if the token is valid, false otherwise
     */
    public boolean isTokenValid(String token) {
        try {
            extractUsername(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}