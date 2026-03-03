package com.mytraxo.auth.controller;

import com.mytraxo.auth.dto.*;
import com.mytraxo.auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody @Valid RegisterRequest req) {
    authService.register(req);
    return ResponseEntity.ok("User registered successfully");
  }

  @PostMapping("/login")
  public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequest req) {
    return ResponseEntity.ok(authService.login(req.getEmail(), req.getPassword()));
  }

  @PostMapping("/refresh")
  public ResponseEntity<AuthResponse> refresh(@RequestBody @Valid RefreshRequest req) {
    return ResponseEntity.ok(authService.refresh(req.getRefreshToken()));
  }

  @PostMapping("/logout")
  public ResponseEntity<?> logout(@RequestBody @Valid RefreshRequest req) {
    authService.logout(req.getRefreshToken());
    return ResponseEntity.ok("Logged out");
  }
}