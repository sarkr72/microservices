package com.apiGateway.apiGateway.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Component
public class JwtUtil {
    private static final Logger log = LoggerFactory.getLogger(JwtUtil.class);

    @Value("${jwt.secret}")
    private String jwtSecret;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token);
            log.debug("Token validated successfully: {}", token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Invalid or expired JWT token: {}", e.getMessage(), e);
            return false;
        }
    }

    public String extractUsername(String token) {
        try {
            Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
            String username = claims.getSubject();
            if (username == null || username.isEmpty()) {
                log.warn("No username (sub claim) found in token");
                return null;
            }
            log.debug("Extracted username: {}", username);
            return username;
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Error extracting username from token: {}", e.getMessage(), e);
            return null;
        }
    }

    public List<String> extractRoles(String token) {
        try {
            Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
            Object rolesClaim = claims.get("roles");
            List<String> roles = new ArrayList<>();
            if (rolesClaim instanceof List) {
                roles = (List<String>) rolesClaim;
            } else if (rolesClaim instanceof String) {
                roles.add((String) rolesClaim);
            }
            log.debug("Extracted roles: {}", roles);
            return roles;
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Error extracting roles from token: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }
}