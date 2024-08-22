package org.example.baba.exception.exceptionType;

import org.springframework.http.HttpStatus;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum PostExceptionType implements ExceptionType {

  // 404 Not Found
  NOT_FOUND_POST(HttpStatus.NOT_FOUND, "요청하신 게시글을 찾을 수 없습니다");

  private final HttpStatus status;
  private final String message;

  @Override
  public HttpStatus status() {
    return null;
  }

  @Override
  public String message() {
    return null;
  }
}
