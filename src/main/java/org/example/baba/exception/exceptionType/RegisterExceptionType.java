package org.example.baba.exception.exceptionType;

import org.springframework.http.HttpStatus;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum RegisterExceptionType implements ExceptionType {

  // 409 Conflic
  // 중복된 계정
  DUPLICATED_MEMBER_NAME(HttpStatus.CONFLICT, "이미 사용 중인 계정입니다."),
  // 중복된 이메일
  DUPLICATED_EMAIL(HttpStatus.CONFLICT, "이미 사용 중인 이메일입니다.");

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
