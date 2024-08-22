package org.example.baba.common.config.dsl;

import org.example.baba.common.security.filter.JwtAuthenticationFilter;
import org.example.baba.common.utils.cookie.CookieUtils;
import org.example.baba.common.utils.jwt.JwtProperties;
import org.example.baba.common.utils.jwt.JwtProvider;
import org.example.baba.common.utils.translator.ObjectMapperUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtFilterDsl extends AbstractHttpConfigurer<JwtFilterDsl, HttpSecurity> {

  // JWT 제공자 및 설정과 관련된 의존성 주입
  private final JwtProvider provider;
  private final JwtProperties properties;
  private final CookieUtils cookieUtils;
  private final ObjectMapperUtils objectMapperUtils;

  @Override
  public void configure(HttpSecurity builder) {
    // AuthenticationManager를 HttpSecurity에서 가져옴
    AuthenticationManager authenticationManager =
        builder.getSharedObject(AuthenticationManager.class);

    // JWT 인증 필터 생성
    JwtAuthenticationFilter jwtAuthenticationFilter =
        new JwtAuthenticationFilter(provider, properties, cookieUtils, objectMapperUtils);
    // 인증 필터의 URL 경로를 설정
    jwtAuthenticationFilter.setFilterProcessesUrl("/api/login");
    // AuthenticationManager 설정
    jwtAuthenticationFilter.setAuthenticationManager(authenticationManager);

    // 필터 체인에 JWT 인증 필터를 추가하고, JWT 검증 필터를 JwtAuthenticationFilter보다 먼저 실행되도록 설정
    builder.addFilter(jwtAuthenticationFilter);
  }

  public JwtFilterDsl build() {
    return this;
  }
}
