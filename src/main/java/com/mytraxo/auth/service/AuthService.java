package com.mytraxo.auth.service;

import com.mytraxo.auth.dto.AuthResponse;
import com.mytraxo.auth.dto.LoginRequest;
import com.mytraxo.auth.dto.RegisterRequest;
import com.mytraxo.auth.entity.RefreshToken;
import com.mytraxo.auth.entity.User;
import com.mytraxo.auth.repo.RefreshTokenRepository;
import com.mytraxo.auth.repo.UserRepository;
import com.mytraxo.auth.security.JwtService;
import com.mytraxo.config.AppProperties;
import com.mytraxo.employee.entity.Employee;
import com.mytraxo.employee.repo.EmployeeRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import com.mytraxo.employee.entity.EmployeeStatus;

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
    private final EmployeeRepository employeeRepository;
  

    // Updated Constructor to include all dependencies
    public AuthService(UserRepository userRepository,
                       RefreshTokenRepository refreshTokenRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       AppProperties props,
                       EmployeeRepository employeeRepository
                       ) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.props = props;
        this.employeeRepository = employeeRepository;
      
    }

    // ✅ Corrected Employee Login
    public AuthResponse employeeLogin(String email, String password) {
      
        // 1. Fetch from Mongo Repo using correct method name
        Employee emp = employeeRepository.findByEmailAddress(email)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
// 🛡️ SECURITY CHECK: Don't allow employees who have "LEFT" to login
    if ("LEFT".equalsIgnoreCase(emp.getEmploymentStatus().name())) {
        throw new RuntimeException("Access Denied: You are no longer an active employee.");
    }
        // 2. Validate phone number as password
        if (!emp.getPhoneNumber().equals(password)) {
            throw new RuntimeException("Invalid Credentials");
        }

        // 3. Generate Tokens (Declared variables properly)
         List<String> roles = List.of("ROLE_EMPLOYEE");
    String access = jwtService.generateAccessToken(emp.getEmailAddress(), roles);
    String refresh = jwtService.generateRefreshToken(emp.getEmailAddress());

    return AuthResponse.builder()
            .accessToken(access)
            .refreshToken(refresh)
            .roles(roles)
            .employeeId(emp.getEmployeeId()) // From Employee Entity
            //.name(emp.getFullName())  // Combine names
           // .status(emp.getEmploymentStatus().name()) // Enum to String
            .build();
    }
public void registerEmployee(Employee employee) {
    // 1. Check if email already exists
    if (employeeRepository.findByEmailAddress(employee.getEmailAddress()).isPresent()) {
        throw new RuntimeException("Employee with this email already exists");
    }

    // 2. Ensure the email is saved in lowercase and trimmed (Clean Data)
    employee.setEmailAddress(employee.getEmailAddress().toLowerCase().trim());
    
    // 3. Set a default status if none is provided
   if (employee.getEmploymentStatus() == null) {
    // Use your Enum (ACTIVE, CURRENT, etc.)
    employee.setEmploymentStatus(com.mytraxo.employee.entity.EmployeeStatus.ACTIVE);
}

    // 4. Save to AWS MongoDB
    employeeRepository.save(employee);

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
        user.setRoles(List.of("USER"));
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
