package org.example.baba.exception.exceptionType;

import org.springframework.http.HttpStatus;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum PostExceptionType implements ExceptionType {

  // 404 Not Found
  NOT_FOUND_POST(HttpStatus.NOT_FOUND, "요청하신 게시글을 찾을 수 없습니다"),
  API_CALL_FAILED(HttpStatus.BAD_GATEWAY, "호출하신 API 가 응답을 주지 않거나 에러를 반환하였습니다."),
  UNSUPPORTED_SNS(HttpStatus.BAD_REQUEST, "지원하지 않는 SNS 입니다.");

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
