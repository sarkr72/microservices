package com.apiGateway.apiGateway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
public class GatewayFallbackController {

    @RequestMapping("/fallback/departments")
    public Mono<ResponseEntity<Map<String, String>>> departFallback() {
        return Mono.just(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Map.of("error", "Service Unavailable",
                             "message", "Department service unavailable (gateway fallback)",
                             "timestamp", String.valueOf(System.currentTimeMillis()))));
    }

    @RequestMapping("/fallback/employees")
    public Mono<ResponseEntity<Map<String, String>>> empFallback() {
        return Mono.just(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Map.of("error", "Service Unavailable",
                             "message", "Employee service unavailable (gateway fallback)",
                             "timestamp", String.valueOf(System.currentTimeMillis()))));
    }
}