package com.pulse.auth.controller;

import com.pulse.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * Registers a new user with the provided username and password.
     *
     * @param body map containing "username" and "password" fields
     * @return 200 OK with success message
     */
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Map<String, String> body) {
        authService.register(body.get("username"), body.get("password"));
        return ResponseEntity.ok("User registered successfully");
    }

    /**
     * Authenticates a user and returns a JWT token.
     *
     * @param body map containing "username" and "password" fields
     * @return 200 OK with JWT token string
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> body) {
        String token = authService.login(body.get("username"), body.get("password"));
        return ResponseEntity.ok(token);
    }
}