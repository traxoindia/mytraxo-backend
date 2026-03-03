package com.mytraxo.auth.security;

import com.mytraxo.config.AppProperties;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Service
public class JwtService {

  private final AppProperties props;
  private final Key key;

  public JwtService(AppProperties props) {
    this.props = props;
    this.key = Keys.hmacShaKeyFor(props.getJwt().getSecret().getBytes(StandardCharsets.UTF_8));
  }

  public String generateAccessToken(String subjectEmail, List<String> roles) {
    Instant now = Instant.now();
    Instant exp = now.plusSeconds(props.getJwt().getAccessMinutes() * 60L);

    return Jwts.builder()
      .setSubject(subjectEmail)
      .claim("roles", roles)
      .setIssuedAt(Date.from(now))
      .setExpiration(Date.from(exp))
      .signWith(key, SignatureAlgorithm.HS256)
      .compact();
  }

  public String generateRefreshToken(String subjectEmail) {
    Instant now = Instant.now();
    Instant exp = now.plusSeconds(props.getJwt().getRefreshDays() * 24L * 3600L);

    return Jwts.builder()
      .setSubject(subjectEmail)
      .setIssuedAt(Date.from(now))
      .setExpiration(Date.from(exp))
      .signWith(key, SignatureAlgorithm.HS256)
      .compact();
  }

  public Jws<Claims> parse(String token) throws JwtException {
    return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
  }
}