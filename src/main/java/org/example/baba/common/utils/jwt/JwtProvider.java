package org.example.baba.common.utils.jwt;

import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtProvider {

  private final JwtProperties jwtProperties;

  // 액세스 JWT 토큰 생성
  public String generateAccessToken(String subject, Long id, String authorities) {
    // 액세스 토큰 만료시간 설정
    Date expiration = getAccessExpiration();
    // 토큰 생성 후 반환
    return Jwts.builder()
        .subject(subject)
        .expiration(expiration)
        .claims(createClaims(id, authorities))
        .signWith(getEncodedKey())
        .compact();
  }

  // 리프레시 토큰 생성
  public String generateRefreshToken(String subject) {
    return Jwts.builder()
        .subject(subject)
        .expiration(getRefreshExpiration())
        .signWith(getEncodedKey())
        .compact();
  }

  // JWT 토큰에서 클레임 정보 추출
  public Claims getClaims(String token) {
    return Jwts.parser()
        .verifyWith(getEncodedKey())
        .clockSkewSeconds(60)
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }

  // JWT 비밀 키 생성
  private SecretKey getEncodedKey() {
    return Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8));
  }

  // JWT에 담을 정보를 클레임으로 생성
  private Map<String, ?> createClaims(Long id, String authorities) {
    return Map.of("id", id, "authorities", authorities);
  }

  // JWT 토큰 만료 시간 계산 후 반환
  private Date getAccessExpiration() {
    return addExpirationData(jwtProperties.getAccessTokenValidityInSeconds()).getTime();
  }

  // 리프레시 토큰 만료 시간 계산 후 반환
  private Date getRefreshExpiration() {
    return addExpirationData(jwtProperties.getRefreshTokenValidityInSeconds()).getTime();
  }

  // 현재 시간에 지정한 분(MINUTE)을 더하여 새로운 시간 반환
  private Calendar addExpirationData(Integer expirationMinutes) {
    Calendar now = Calendar.getInstance();
    now.add(Calendar.SECOND, expirationMinutes);
    return now;
  }
}
