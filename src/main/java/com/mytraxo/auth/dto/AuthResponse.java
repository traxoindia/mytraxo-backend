// AuthResponse.java
package com.mytraxo.auth.dto;
import lombok.Builder;
import lombok.Getter;
import java.util.List;
import lombok.AllArgsConstructor; // 👈 Add this
import lombok.NoArgsConstructor;

@AllArgsConstructor // 👈 Add this
@NoArgsConstructor  
@Getter
@Builder
public class AuthResponse {
  private  String accessToken;
  private  String refreshToken;
  private  List<String> roles;
  // ADD THESE FIELDS
    private String employeeId;
    private String name;
    //private String status;
}

