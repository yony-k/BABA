package org.example.baba.common.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.*;

import org.example.baba.common.security.dto.LoginDto;
import org.example.baba.common.utils.jwt.*;
import org.example.baba.common.utils.translator.ObjectMapperUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.*;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private final ObjectMapperUtils objectMapper;

  // 로그인 요청 데이터를 받아서 토큰 생성하고 인증 시도
  @Override
  public Authentication attemptAuthentication(
      HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
    LoginDto requestData = objectMapper.toEntity(request, LoginDto.class);
    var authenticationToken = createAuthenticationToken(requestData);

    return getAuthenticationManager().authenticate(authenticationToken);
  }

  // 인증 성공 후 액세스/리프레시 토큰 생성 후 응답
  @Override
  protected void successfulAuthentication(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain chain,
      Authentication authResult) {}

  // 로그인 DTO를 기반으로 스프링 시큐리티 인증 객체 생성
  private UsernamePasswordAuthenticationToken createAuthenticationToken(LoginDto login) {
    return new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword());
  }
}
