package org.example.baba.common.security.filter;

import java.util.Collection;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.*;

import org.example.baba.common.security.details.AuthUser;
import org.example.baba.common.security.dto.LoginDto;
import org.example.baba.common.utils.cookie.CookieUtils;
import org.example.baba.common.utils.jwt.*;
import org.example.baba.common.utils.translator.ObjectMapperUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.*;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StringUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private final JwtProvider jwtProvider;
  private final JwtProperties jwtProperties;
  private final CookieUtils cookieUtils;
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
      Authentication authResult) {
    AuthUser authUser = (AuthUser) authResult.getPrincipal();

    // 토큰 생성
    String accessToken = createAccessToken(authUser);
    String refreshToken = createRefreshToken(authUser);

    // 응답 헤더에 액세스 토큰 설정
    response.setHeader(HttpHeaders.AUTHORIZATION, jwtProperties.getPrefix() + accessToken);
    // 응답 쿠키에 리프레시 토큰 추가
    response.addCookie(cookieUtils.createCookie(refreshToken));
  }

  // 액세스 토큰 생성
  private String createAccessToken(AuthUser authUser) {
    return jwtProvider.generateAccessToken(
        authUser.getUsername(), authUser.getId(), toTrans(authUser.getAuthorities()));
  }

  // 리프레시 토큰 생성
  private String createRefreshToken(AuthUser jwtAuthUser) {
    return jwtProvider.generateRefreshToken(jwtAuthUser.getUsername());
  }

  // 권한 리스트를 문자열로 변환
  private String toTrans(Collection<GrantedAuthority> list) {
    return StringUtils.collectionToCommaDelimitedString(list);
  }

  // 로그인 DTO를 기반으로 스프링 시큐리티 인증 객체 생성
  private UsernamePasswordAuthenticationToken createAuthenticationToken(LoginDto login) {
    return new UsernamePasswordAuthenticationToken(login.email(), login.password());
  }
}
