package com.mytraxo.dashboard;

import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

  @GetMapping("/admin/summary")
  public Map<String, Object> adminSummary() {
    return Map.of("dashboard", "ADMIN", "message", "Admin summary data");
  }

  @GetMapping("/hr/summary")
  public Map<String, Object> hrSummary() {
    return Map.of("dashboard", "HR", "message", "HR summary data");
  }

  @GetMapping("/user/summary")
  public Map<String, Object> userSummary() {
    return Map.of("dashboard", "USER", "message", "User summary data");
  }
}