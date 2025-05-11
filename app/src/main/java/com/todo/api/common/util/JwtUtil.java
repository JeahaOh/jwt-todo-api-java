package com.todo.api.common.util;

import com.todo.api.common.config.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtUtil {
  private final JwtConfig jwtConfig;
  private Key key;

  private Key getSigningKey() {
    if (key == null) {
      key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    }
    return key;
  }

  public String generateToken(String email, Integer memberNo) {
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + jwtConfig.getAccessTokenValidityInMinutes() * 60 * 1000);

    Map<String, Object> claims = new HashMap<>();
    claims.put("memberNo", memberNo);

    return Jwts.builder()
        .setClaims(claims)
        .setSubject(email)
        .setIssuedAt(now)
        .setExpiration(expiryDate)
        .signWith(getSigningKey(), SignatureAlgorithm.HS512)
        .compact();
  }

  public String getEmailFromToken(String token) {
    Claims claims = Jwts.parserBuilder()
        .setSigningKey(getSigningKey())
        .build()
        .parseClaimsJws(token)
        .getBody();

    return claims.getSubject();
  }

  public Integer getMemberNoFromToken(String token) {
    Claims claims = Jwts.parserBuilder()
        .setSigningKey(getSigningKey())
        .build()
        .parseClaimsJws(token)
        .getBody();

    return claims.get("memberNo", Integer.class);
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parserBuilder()
          .setSigningKey(getSigningKey())
          .build()
          .parseClaimsJws(token);
      return true;
    } catch (Exception e) {
      return false;
    }
  }
}