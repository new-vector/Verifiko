package com.verifico.server.auth.JWT;

import java.time.Instant;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;

@Service
public class JWTService {

  // set this value, on my pc
  @Value("${JWT_SECRET}")
  private String jwtSecret;

  @Value("${JWT_EXPIRY}")
  private int accessTokenMins;

  private SecretKey getSigningKey() {
    byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  private Claims extractAllClaims(String token) {
    return Jwts.parser().verifyWith(getSigningKey()).build().parseClaimsJws(token).getPayload();
  }

  public String generateAccessToken(Long userId, String username) {
    return Jwts.builder()
        .subject(userId.toString())
        .claim("username", username)
        .issuedAt(new Date())
        .expiration(Date.from(Instant.now().plusSeconds(accessTokenMins * 60)))
        .signWith(getSigningKey())
        .compact();
  }

  public boolean validateAccessToken(String token) {
    try {
      extractAllClaims(token);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  public Long getUserIdFromToken(String token) {
    Claims claims = extractAllClaims(token);
    return Long.parseLong(claims.getSubject());
  }

  public String getUsernameFromToken(String token) {
    Claims claims = extractAllClaims(token);
    return claims.get("username", String.class);
  }
}
