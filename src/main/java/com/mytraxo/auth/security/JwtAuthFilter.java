package com.mytraxo.auth.security;

import com.mytraxo.auth.entity.User;
import com.mytraxo.auth.repo.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  private final UserRepository userRepository;

  public JwtAuthFilter(JwtService jwtService, UserRepository userRepository) {
    this.jwtService = jwtService;
    this.userRepository = userRepository;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain chain)
      throws ServletException, IOException {

    // Skip auth endpoints
    String path = request.getRequestURI();
    if (path.startsWith("/api/auth")) {
      chain.doFilter(request, response);
      return;
    }

    // Already authenticated? Skip
    if (SecurityContextHolder.getContext().getAuthentication() != null) {
      chain.doFilter(request, response);
      return;
    }

    String authHeader = request.getHeader("Authorization");

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      chain.doFilter(request, response);
      return;
    }

    String token = authHeader.substring(7);

    try {
      Jws<Claims> parsed = jwtService.parse(token);
      String email = parsed.getBody().getSubject();

      User user = userRepository.findByEmail(email).orElse(null);

      if (user == null || !user.isEnabled()) {
        chain.doFilter(request, response);
        return;
      }

      UserPrincipal principal = new UserPrincipal(
          user.getEmail(),
          user.getPasswordHash(),
          user.isEnabled(),
          user.getRoles()
      );

      UsernamePasswordAuthenticationToken authentication =
          new UsernamePasswordAuthenticationToken(
              principal,
              null,
              principal.getAuthorities()
          );

      SecurityContextHolder.getContext().setAuthentication(authentication);

    } catch (JwtException e) {
      // Invalid or expired token — ignore and continue
    }

    chain.doFilter(request, response);
  }
}