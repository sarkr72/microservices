//package com.authService.auth_service.config;
//
//import java.util.Base64; // Add import
//import java.util.Date;
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.security.Keys;
//
//@Component
//public class JwtUtil {
//    @Value("${jwt.secret}")
//    private String secret;
//
//    public JwtUtil() {
//    }
//
//    public String generateToken(String username, List<String> roles) {
//        // Decode Base64 secret
//        byte[] keyBytes = Base64.getDecoder().decode(secret);
//        return Jwts.builder()
//                .subject(username)
//                .claim("roles", roles)
//                .issuedAt(new Date())
//                .expiration(new Date(System.currentTimeMillis() + 86400000))
//                .signWith(Keys.hmacShaKeyFor(keyBytes))
//                .compact();
//    }
//}

package com.authService.auth_service.config;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secret;

    public JwtUtil() {
    }

    public String generateToken(String username, List<String> roles) {
        return Jwts.builder()
                .subject(username)
                .claim("roles", roles)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
                .compact();
    }
}