package com.apiGateway.apiGateway.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtFilter extends AbstractGatewayFilterFactory<JwtFilter.Config> {
    private static final Logger log = LoggerFactory.getLogger(JwtFilter.class);
    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        super(Config.class);
        this.jwtUtil = jwtUtil;
        log.info("JwtFilter initialized");
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String path = exchange.getRequest().getURI().getPath();
            log.debug("Processing request: path={}", path);

            // Allow unauthenticated access to specific paths
            if (path.startsWith("/auth/") || path.startsWith("/actuator/") || path.startsWith("/eureka/") || path.startsWith("/h2-console/")) {
                log.debug("Allowing unauthenticated access to path: {}", path);
                return chain.filter(exchange);
            }

            // Extract Authorization header
            String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            log.debug("Authorization header: {}", authHeader);
            if (authHeader == null || !authHeader.toLowerCase().startsWith("bearer ")) {
                log.warn("Missing or malformed Authorization header: {}", authHeader);
                return writeUnauthorized(exchange, "Unauthorized: Missing or malformed Bearer token");
            }

            // Extract token
            String token = authHeader.substring(7).trim();
            if (token.isEmpty()) {
                log.warn("Empty token after extracting from Authorization header");
                return writeUnauthorized(exchange, "Unauthorized: Empty token");
            }
            log.debug("Extracted token: {}", token);

            // Validate token
            if (!jwtUtil.validateToken(token)) {
                log.warn("Token validation failed for token: {}", token);
                return writeUnauthorized(exchange, "Unauthorized: Invalid or expired token");
            }

            // Extract username and roles
            String username = jwtUtil.extractUsername(token);
            if (username == null || username.isEmpty()) {
                log.warn("No username found in token");
                return writeUnauthorized(exchange, "Unauthorized: Invalid token - no username");
            }

            List<SimpleGrantedAuthority> authorities = jwtUtil.extractRoles(token).stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
            log.info("Authenticated user: {}, authorities: {}", username, authorities);

            // Create authentication token
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                username, null, authorities
            );
            SecurityContext securityContext = new SecurityContextImpl(auth);

            // Mutate exchange to include username in headers
            ServerWebExchange mutatedExchange = exchange.mutate()
                .request(exchange.getRequest().mutate()
                    .header("X-Auth-Username", username)
                    .build())
                .build();

            // Continue with the filter chain, including the security context
            return chain.filter(mutatedExchange)
                .contextWrite(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(securityContext)))
                .onErrorResume(e -> {
                    log.error("Error in JwtFilter processing: {}", e.getMessage(), e);
                    return writeUnauthorized(exchange, "Unauthorized: Error processing token");
                });
        };
    }

    private Mono<Void> writeUnauthorized(ServerWebExchange exchange, String message) {
        log.warn("Rejecting request: {}", message);
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        byte[] bytes = ("{\"error\":\"" + message + "\"}").getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
        return exchange.getResponse().writeWith(Mono.just(buffer));
    }

    public static class Config {}
}