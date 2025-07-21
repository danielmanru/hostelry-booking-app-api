package com.daytoday.hostelrybooking.security.jwt;

import com.daytoday.hostelrybooking.security.user.WebUserDetails;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JwtUtils {
  @Value("${access.token.secret}")
  private String jwtSecret;
  @Value("${expiration.time}")
  private Integer expirationTime;

  public String generateToken(Authentication authentication) {
    WebUserDetails userPrincipal = (WebUserDetails) authentication.getPrincipal();

    List<String> role = userPrincipal.getAuthorities()
        .stream()
        .map(GrantedAuthority::getAuthority).toList();

    Map<String, Object> claims = new HashMap<>();
    claims.put("id", userPrincipal.getId());
    claims.put("role", role);

    Instant now = Instant.now();

    return Jwts.builder()
        .claims(claims)
        .subject(userPrincipal.getEmail())
        .issuedAt(Date.from(now))
        .expiration(Date.from(now.plusMillis(expirationTime)))
        .signWith(key())
        .compact();
  }

  public String getUsernameFromToken(String token) {
    return Jwts.parser()
        .verifyWith(key())
        .build()
        .parseSignedClaims(token)
        .getPayload()
        .getSubject();
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parser()
          .verifyWith(key())
          .build()
          .parseSignedClaims(token);
      return true;
    } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SecurityException | IllegalArgumentException e) {
      throw new JwtException(e.getMessage());
    }
  }

  private SecretKey key() {
    return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
  }
}
