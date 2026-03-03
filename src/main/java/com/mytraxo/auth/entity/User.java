package com.mytraxo.auth.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "users")
@Getter @Setter
public class User {
  @Id
  private String id;

  private String email;
  private String passwordHash;
  private boolean enabled = true;

  // Store role names directly: ADMIN / HR / USER
  private List<String> roles = new ArrayList<>();
}