package com.mytraxo.config;

import com.mytraxo.auth.security.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
public class SecurityConfig {

  private final JwtAuthFilter jwtAuthFilter;
  private final AppProperties props;

  public SecurityConfig(JwtAuthFilter jwtAuthFilter, AppProperties props) {
    this.jwtAuthFilter = jwtAuthFilter;
    this.props = props;
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
      .cors(cors -> cors.configurationSource(req -> {
        CorsConfiguration c = new CorsConfiguration();
        c.setAllowedOrigins(props.getCors().getAllowedOrigins());
        c.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));
        c.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        c.setExposedHeaders(List.of("Authorization"));
        c.setAllowCredentials(true);
        return c;
      }))
      .csrf(csrf -> csrf.disable())
      .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      .authorizeHttpRequests(auth -> auth

        // ✅ Allow browser preflight
        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

        // ✅ Public endpoints
        .requestMatchers("/actuator/health").permitAll()
        .requestMatchers("/api/auth/**").permitAll()
		// Career public APIs
        .requestMatchers("/api/careers/**").permitAll()

        // ✅ Role protected routes
        .requestMatchers("/api/admin/**").hasRole("ADMIN")
        .requestMatchers("/api/hr/**").hasRole("HR")
        .requestMatchers("/api/user/**").hasRole("USER")

        .anyRequest().authenticated()
      );

    http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}