package org.example.baba.exception.exceptionType;

import org.springframework.http.HttpStatus;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum AuthorizedExceptionType implements ExceptionType {

  // 인증 에러
  ACCESS_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, " Access Token 만료"),
  INVALID_SIGNATURE_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "서명이 올바르지 않습니다"),
  UNAUTHENTICATED(HttpStatus.UNAUTHORIZED, "접근 권한이 없습니다."),
  REFRESH_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "저장된 RefreshToken이 존재하지 않습니다"),
  REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "RefreshToken 기간이 만료 되었습니다");

  private final HttpStatus status;
  private final String message;

  @Override
  public HttpStatus status() {
    return this.status;
  }

  @Override
  public String message() {
    return this.message;
  }
}
