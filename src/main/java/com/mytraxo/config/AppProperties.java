package com.mytraxo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "app")
public class AppProperties {
  private Jwt jwt = new Jwt();
  private Cors cors = new Cors();

  @Data
  public static class Jwt {
    private String secret;
    private int accessMinutes = 15;
    private int refreshDays = 30;
  }

  @Data
  public static class Cors {
    private List<String> allowedOrigins;
  }
}