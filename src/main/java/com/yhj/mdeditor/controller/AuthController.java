package com.yhj.mdeditor.controller;

import com.yhj.mdeditor.dto.AuthResponse;
import com.yhj.mdeditor.dto.LoginRequest;
import com.yhj.mdeditor.dto.RegisterRequest;
import com.yhj.mdeditor.entity.User;
import com.yhj.mdeditor.exception.UserAlreadyExistsException;
import com.yhj.mdeditor.service.UserService;
import com.yhj.mdeditor.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

/**
 * Authentication REST Controller
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    /**
     * Register a new user
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        try {
            User user = userService.register(request);
            String token = jwtUtil.generateToken(user.getUsername(), user.getId());

            return ResponseEntity.ok(AuthResponse.builder()
                .token(token)
                .username(user.getUsername())
                .userId(user.getId())
                .message("Registration successful")
                .build());
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(AuthResponse.builder()
                    .message(e.getMessage())
                    .build());
        }
    }

    /**
     * Login user
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            User user = userService.findByUsername(request.getUsername()).orElseThrow();
            String token = jwtUtil.generateToken(user.getUsername(), user.getId());

            return ResponseEntity.ok(AuthResponse.builder()
                .token(token)
                .username(user.getUsername())
                .userId(user.getId())
                .message("Login successful")
                .build());
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(AuthResponse.builder()
                    .message("Invalid credentials")
                    .build());
        }
    }

    /**
     * Logout user
     */
    @PostMapping("/logout")
    public ResponseEntity<AuthResponse> logout() {
        return ResponseEntity.ok(AuthResponse.builder()
            .message("Logout successful")
            .build());
    }
}