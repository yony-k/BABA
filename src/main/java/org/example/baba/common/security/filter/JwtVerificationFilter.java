package org.example.baba.common.security.filter;

import java.io.IOException;

import jakarta.servlet.*;
import jakarta.servlet.http.*;

import org.example.baba.common.security.details.AuthUser;
import org.example.baba.common.utils.jwt.*;
import org.example.baba.exception.exceptionType.AuthorizedExceptionType;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtVerificationFilter extends OncePerRequestFilter {

  private static final String EXCEPTION_CODE = "exceptionCode";
  private final JwtProvider jwtProvider;
  private final JwtProperties jwtProperties;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    try {
      setAuthenticationToContext(request);
    } catch (ExpiredJwtException ee) { // JWT 토큰 만료된 경우
      request.setAttribute(EXCEPTION_CODE, AuthorizedExceptionType.ACCESS_TOKEN_EXPIRED);
    } catch (SignatureException se) { // JWT 토큰 서명이 유효하지 않을 경우
      request.setAttribute(EXCEPTION_CODE, AuthorizedExceptionType.INVALID_SIGNATURE_ACCESS_TOKEN);
    } catch (JwtException e) { // JWT 기타 예외
      request.setAttribute(EXCEPTION_CODE, AuthorizedExceptionType.UNAUTHENTICATED);
    }
    filterChain.doFilter(request, response);
  }

  // 인증 정보 생성
  private void setAuthenticationToContext(HttpServletRequest request) {
    SecurityContextHolder.getContext().setAuthentication(createAuthenticatiedToken(request));
  }

  // 사용자 정보 기반으로 인증 토큰 생성
  private Authentication createAuthenticatiedToken(HttpServletRequest request) {
    AuthUser authUser = createUserDetails(request);
    return new UsernamePasswordAuthenticationToken(
        authUser.getId(), null, authUser.getAuthorities());
  }

  // JWT 토큰에서 사용자 정보 생성 후 반환
  private AuthUser createUserDetails(HttpServletRequest request) {
    String token =
        getAuthenticationTokenToHeader(request).substring(jwtProperties.getPrefixLength());
    return new AuthUser(jwtProvider.getClaims(token));
  }

  // Authentication 조회
  private String getAuthenticationTokenToHeader(HttpServletRequest request) {
    return request.getHeader(HttpHeaders.AUTHORIZATION);
  }
}
