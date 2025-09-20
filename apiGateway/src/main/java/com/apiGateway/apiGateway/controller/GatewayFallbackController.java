package com.apiGateway.apiGateway.controller;

import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

//@RestController
//public class GatewayFallbackController {
//    @RequestMapping("/fallback/departments")
//    public Mono<ResponseEntity<Map<String, String>>> departFallback() {
//        return Mono.just(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
//                .body(Map.of("message", "Department service unavailable (gateway fallback)")));
//    }
//
//    @RequestMapping("/fallback/employees")
//    public Mono<ResponseEntity<Map<String, String>>> empFallback() {
//        return Mono.just(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
//                .body(Map.of("message", "Employee service unavailable (gateway fallback)")));
//    }
//}


import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class GatewayFallbackController {
    private static final Logger logger = LoggerFactory.getLogger(GatewayFallbackController.class);
    private final Tracer tracer;

    public GatewayFallbackController(Tracer tracer) {
        this.tracer = tracer;
    }

    @RequestMapping("/fallback/departments")
    public Mono<ResponseEntity<Map<String, String>>> departFallback() {
        Span span = tracer.spanBuilder("gateway.fallback.departments").startSpan();
        try (var scope = span.makeCurrent()) {
            logger.warn("Department service fallback triggered at gateway");
            return Mono.just(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(Map.of("error", "Service Unavailable",
                                 "message", "Department service unavailable (gateway fallback)",
                                 "timestamp", System.currentTimeMillis() + "")));
        } finally {
            span.end();
        }
    }

    @RequestMapping("/fallback/employees")
    public Mono<ResponseEntity<Map<String, String>>> empFallback() {
        Span span = tracer.spanBuilder("gateway.fallback.employees").startSpan();
        try (var scope = span.makeCurrent()) {
            logger.warn("Employee service fallback triggered at gateway");
            return Mono.just(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(Map.of("error", "Service Unavailable",
                                 "message", "Employee service unavailable (gateway fallback)",
                                 "timestamp", System.currentTimeMillis() + "")));
        } finally {
            span.end();
        }
    }
}