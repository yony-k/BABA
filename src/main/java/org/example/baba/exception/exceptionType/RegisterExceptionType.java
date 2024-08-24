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
  DUPLICATED_EMAIL(HttpStatus.CONFLICT, "이미 사용 중인 이메일입니다."),
  // Redis에 이미 저장된 사용자 정보가 있을 때
  EMAIL_ALREADY_IN_PROGRESS(
      HttpStatus.CONFLICT, "이 이메일로 가입이 진행중입니다. 이메일 인증을 완료하거나 일정 시간 후에 다시 시도해주세요."),

  // 400 Bad Request
  // 저장된 코드가 없을 때
  NOT_FOUND_CODE(HttpStatus.BAD_REQUEST, "가입승인 코드가 유효하지 않습니다. 다시 시도해주세요."),
  // 저장된 임시 데이터가 없을 때
  NOT_FOUND_MEMBER(HttpStatus.BAD_REQUEST, "가입 가능 기간이 만료되었습니다. 다시 가입을 시도해주세요."),

  // 500 Internal Server Error
  // 코드 발송한 이메일과 임시 저장된 이메일 데이터 불일치
  DATA_CONSISTENCY_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류입니다. 다시 시도해주세요.");

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
