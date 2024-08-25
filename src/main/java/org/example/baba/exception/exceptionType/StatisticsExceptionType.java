package org.example.baba.exception.exceptionType;

import org.springframework.http.HttpStatus;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum StatisticsExceptionType implements ExceptionType {
  INVALID_DATE_RANGE_EXCEPTION(HttpStatus.BAD_REQUEST, "날짜의 범위가 올바르지 않습니다. 최대 30일까지 조회가 가능합니다."),
  INVALID_HOUR_RANGE_EXCEPTION(HttpStatus.BAD_REQUEST, "날짜의 범위가 올바르지 않습니다. 최대 7일까지 조회가 가능합니다");

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
