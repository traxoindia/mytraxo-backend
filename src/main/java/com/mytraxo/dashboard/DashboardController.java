package com.mytraxo.dashboard;

import org.springframework.web.bind.annotation.*;

import com.mytraxo.dashboard.service.DashboardService;
import lombok.RequiredArgsConstructor;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dashboard")
public class DashboardController {

  private final DashboardService service;

    @GetMapping("/stats")
    public Map<String, Long> getStats() {
        return service.getStats();
    }

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