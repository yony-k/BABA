package org.example.baba.common.config.dsl;

import org.example.baba.common.redis.RedisRepository;
import org.example.baba.common.security.filter.JwtAuthenticationFilter;
import org.example.baba.common.security.filter.JwtVerificationFilter;
import org.example.baba.common.security.handler.AuthenticationFailureCustomHandler;
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
  private final RedisRepository repository;
  private final ObjectMapperUtils objectMapperUtils;
  private final AuthenticationFailureCustomHandler authenticationFailureCustomHandler;

  @Override
  public void configure(HttpSecurity builder) {
    // AuthenticationManager를 HttpSecurity에서 가져옴
    AuthenticationManager authenticationManager =
        builder.getSharedObject(AuthenticationManager.class);

    // JWT 인증 필터 생성
    JwtAuthenticationFilter jwtAuthenticationFilter =
        new JwtAuthenticationFilter(
            provider, properties, cookieUtils, repository, objectMapperUtils);
    // 인증 필터의 URL 경로를 설정
    jwtAuthenticationFilter.setFilterProcessesUrl("/api/login");
    jwtAuthenticationFilter.setAuthenticationManager(authenticationManager);
    jwtAuthenticationFilter.setAuthenticationFailureHandler(authenticationFailureCustomHandler);

    JwtVerificationFilter jwtVerificationFilter = new JwtVerificationFilter(provider, properties);

    // 필터 체인에 JWT 인증 필터를 추가하고, JWT 검증 필터를 JwtAuthenticationFilter보다 먼저 실행되도록 설정
    // 정상작동하지 않는 문제

    builder
        .addFilter(jwtAuthenticationFilter)
        .addFilterBefore(jwtVerificationFilter, JwtAuthenticationFilter.class);
  }

  public JwtFilterDsl build() {
    return this;
  }
}
