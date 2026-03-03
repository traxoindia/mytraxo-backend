package com.mytraxo.user;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class MeController {

  @GetMapping("/api/me")
  public Map<String, Object> me(Authentication auth) {
    return Map.of(
      "email", auth.getName(),
      "authorities", auth.getAuthorities()
    );
  }
}