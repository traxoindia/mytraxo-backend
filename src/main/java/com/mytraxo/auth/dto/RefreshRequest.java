// RefreshRequest.java
package com.mytraxo.auth.dto;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter; import lombok.Setter;

@Getter @Setter
public class RefreshRequest {
  @NotBlank
  private String refreshToken;
}