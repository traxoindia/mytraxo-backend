package com.mytraxo.api;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/hr")
public class HrController {
  @GetMapping("/summary")
  public String summary() {
    return "HR summary OK";
  }
}