package com.mytraxo.api;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

  @GetMapping("/summary")
  public String summary() {
    return "admin summary";
  }
}