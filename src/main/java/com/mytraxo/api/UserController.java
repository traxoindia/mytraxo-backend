package com.mytraxo.api;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
  @GetMapping("/summary")
  public String summary() {
    return "USER summary OK";
  }
}