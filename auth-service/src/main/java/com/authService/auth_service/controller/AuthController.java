package com.authService.auth_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.authService.auth_service.model.AuthRequest;
import com.authService.auth_service.model.User;
import com.authService.auth_service.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {
	private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        return ResponseEntity.ok(authService.register(user));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthRequest user) {
        return ResponseEntity.ok(authService.login(user));
    }
}