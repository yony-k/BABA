package org.example.baba.common.security.config;

import org.example.baba.common.redis.RedisRepository;
import org.example.baba.common.security.details.MemberDetailService;
import org.example.baba.common.security.handler.AuthenticationEntryPointHandler;
import org.example.baba.common.security.handler.AuthenticationFailureCustomHandler;
import org.example.baba.common.security.handler.LogoutSuccessCustomHandler;
import org.example.baba.common.security.handler.VerificationAccessDeniedHandler;
import org.example.baba.common.security.service.AuthService;
import org.example.baba.common.utils.cookie.CookieProperties;
import org.example.baba.common.utils.cookie.CookieUtils;
import org.example.baba.common.utils.jwt.JwtProperties;
import org.example.baba.common.utils.jwt.JwtProvider;
import org.example.baba.common.utils.translator.ObjectMapperUtils;
import org.example.baba.repository.MemberRepository;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.fasterxml.jackson.databind.ObjectMapper;

@TestConfiguration
public class SecurityTestConfig {
  @Bean
  public JwtProvider jwtProvider() {
    return new JwtProvider(jwtProperties());
  }

  @Bean
  public JwtProperties jwtProperties() {
    return new JwtProperties();
  }

  @Bean
  public ObjectMapper objectMapper() {
    return new ObjectMapper();
  }

  @Bean
  public CookieProperties cookieProperties() {
    return new CookieProperties();
  }

  @Bean
  public CookieUtils cookieUtils() {
    return new CookieUtils(cookieProperties());
  }

  @Bean
  public AuthenticationFailureCustomHandler authenticationFailureCustomHandler() {
    return new AuthenticationFailureCustomHandler(objectMapperUtils());
  }

  @Bean
  public AuthenticationEntryPointHandler authenticationEntryPointHandler() {
    return new AuthenticationEntryPointHandler(objectMapperUtils());
  }

  @Bean
  public LogoutSuccessCustomHandler logoutSuccessCustomHandler() {
    return new LogoutSuccessCustomHandler(cookieUtils(), redisRepository());
  }

  @Bean
  public VerificationAccessDeniedHandler verificationAccessDeniedHandler() {
    return new VerificationAccessDeniedHandler(objectMapperUtils());
  }

  @Bean
  public UserDetailsService userDetailsService() {
    return new MemberDetailService(memberRepository());
  }

  @Bean
  public AuthService authService() {
    return new AuthService(
        redisRepository(),
        jwtProvider(),
        jwtProperties(),
        memberRepository(),
        objectMapperUtils(),
        cookieUtils());
  }

  @Bean
  public ObjectMapperUtils objectMapperUtils() {
    return new ObjectMapperUtils(objectMapper());
  }

  @Bean
  public MemberRepository memberRepository() {
    return Mockito.mock(MemberRepository.class);
  }

  @Bean
  public RedisRepository redisRepository() {
    return Mockito.mock(RedisRepository.class);
  }
}
