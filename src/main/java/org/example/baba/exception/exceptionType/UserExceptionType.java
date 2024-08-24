package org.example.baba.exception.exceptionType;

import org.springframework.http.HttpStatus;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum UserExceptionType implements ExceptionType {
  USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다.");

  private final HttpStatus httpStatus;
  private final String message;

  @Override
  public HttpStatus status() {
    return this.httpStatus;
  }

  @Override
  public String message() {
    return this.message;
  }
}
