package com.apiGateway.apiGateway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fallback")
public class GatewayFallbackController {

    @GetMapping("/departments")
    public ResponseEntity<String> departmentFallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Department Service is unavailable. Please try again later.");
    }

    @GetMapping("/employees")
    public ResponseEntity<String> employeeFallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Employee Service is unavailable. Please try again later.");
    }
}
