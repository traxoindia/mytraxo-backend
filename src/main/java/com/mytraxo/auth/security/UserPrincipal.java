package com.mytraxo.auth.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class UserPrincipal implements UserDetails {

  private final String email;
  private final String passwordHash;
  private final boolean enabled;
  private final List<String> roles;

  public UserPrincipal(String email, String passwordHash, boolean enabled, List<String> roles) {
    this.email = email;
    this.passwordHash = passwordHash;
    this.enabled = enabled;
    this.roles = roles;
  }

  @Override public Collection<? extends GrantedAuthority> getAuthorities() {
    return roles.stream().map(r -> new SimpleGrantedAuthority("ROLE_" + r)).toList();
  }
  @Override public String getPassword() { return passwordHash; }
  @Override public String getUsername() { return email; }
  @Override public boolean isAccountNonExpired() { return true; }
  @Override public boolean isAccountNonLocked() { return true; }
  @Override public boolean isCredentialsNonExpired() { return true; }
  @Override public boolean isEnabled() { return enabled; }
}