package com.mytraxo.auth.service;

import com.mytraxo.auth.dto.AuthResponse;
import com.mytraxo.auth.dto.RegisterRequest;
import com.mytraxo.auth.entity.RefreshToken;
import com.mytraxo.auth.entity.User;
import com.mytraxo.auth.repo.RefreshTokenRepository;
import com.mytraxo.auth.repo.UserRepository;
import com.mytraxo.auth.security.JwtService;
import com.mytraxo.config.AppProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.List;

@Service
public class AuthService {

  private final UserRepository userRepository;
  private final RefreshTokenRepository refreshTokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AppProperties props;

  public AuthService(UserRepository userRepository,
                     RefreshTokenRepository refreshTokenRepository,
                     PasswordEncoder passwordEncoder,
                     JwtService jwtService,
                     AppProperties props) {
    this.userRepository = userRepository;
    this.refreshTokenRepository = refreshTokenRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtService = jwtService;
    this.props = props;
  }

  public void register(RegisterRequest req) {
    String email = req.getEmail().toLowerCase();

    if (userRepository.existsByEmail(email)) {
      throw new RuntimeException("Email already exists");
    }

    User user = new User();
    user.setEmail(email);
    user.setPasswordHash(passwordEncoder.encode(req.getPassword()));
    user.setEnabled(true);
    user.setRoles(List.of("USER")); // default role
    userRepository.save(user);
  }

  public AuthResponse login(String email, String password) {
    User user = userRepository.findByEmail(email.toLowerCase())
      .orElseThrow(() -> new RuntimeException("Invalid credentials"));

    if (!user.isEnabled() || !passwordEncoder.matches(password, user.getPasswordHash())) {
      throw new RuntimeException("Invalid credentials");
    }

    List<String> roles = user.getRoles();
    String access = jwtService.generateAccessToken(user.getEmail(), roles);
    String refresh = jwtService.generateRefreshToken(user.getEmail());

    saveRefreshToken(user, refresh);

    return AuthResponse.builder()
      .accessToken(access)
      .refreshToken(refresh)
      .roles(roles)
      .build();
  }

  public AuthResponse refresh(String refreshToken) {
    Jws<Claims> parsed;
    try {
      parsed = jwtService.parse(refreshToken);
    } catch (JwtException e) {
      throw new RuntimeException("Invalid refresh token");
    }

    String email = parsed.getBody().getSubject();
    String hash = sha256(refreshToken);

    RefreshToken stored = refreshTokenRepository.findByTokenHash(hash)
      .orElseThrow(() -> new RuntimeException("Refresh token not found"));

    if (stored.getRevokedAt() != null || stored.getExpiresAt().isBefore(Instant.now())) {
      throw new RuntimeException("Refresh token expired/revoked");
    }

    User user = userRepository.findByEmail(email)
      .orElseThrow(() -> new RuntimeException("User not found"));

    // rotate refresh
    stored.setRevokedAt(Instant.now());
    refreshTokenRepository.save(stored);

    String newRefresh = jwtService.generateRefreshToken(email);
    saveRefreshToken(user, newRefresh);

    String newAccess = jwtService.generateAccessToken(email, user.getRoles());

    return AuthResponse.builder()
      .accessToken(newAccess)
      .refreshToken(newRefresh)
      .roles(user.getRoles())
      .build();
  }

  public void logout(String refreshToken) {
    String hash = sha256(refreshToken);
    refreshTokenRepository.findByTokenHash(hash).ifPresent(rt -> {
      rt.setRevokedAt(Instant.now());
      refreshTokenRepository.save(rt);
    });
  }

  private void saveRefreshToken(User user, String rawToken) {
    Instant expires = Instant.now().plusSeconds(props.getJwt().getRefreshDays() * 24L * 3600L);
    RefreshToken rt = RefreshToken.builder()
      .userId(user.getId())
      .tokenHash(sha256(rawToken))
      .expiresAt(expires)
      .revokedAt(null)
      .build();
    refreshTokenRepository.save(rt);
  }

  private static String sha256(String value) {
    try {
      MessageDigest md = MessageDigest.getInstance("SHA-256");
      byte[] digest = md.digest(value.getBytes(StandardCharsets.UTF_8));
      StringBuilder sb = new StringBuilder();
      for (byte b : digest) sb.append(String.format("%02x", b));
      return sb.toString();
    } catch (Exception e) {
      throw new RuntimeException("Hash error");
    }
  }
}