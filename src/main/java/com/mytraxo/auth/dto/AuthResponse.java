// AuthResponse.java
package com.mytraxo.auth.dto;
import lombok.Builder;
import lombok.Getter;
import java.util.List;

@Getter
@Builder
public class AuthResponse {
  private final String accessToken;
  private final String refreshToken;
  private final List<String> roles;
}