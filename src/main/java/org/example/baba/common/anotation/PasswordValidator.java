package org.example.baba.common.anotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

// @ValidPassword 어노테이션에서 사용될 비밀번호 검증 클래스
public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {

  // 비밀번호가 10자 이상, 숫자, 문자, 특수문자가 두가지 이상 포함되는 정규식표현
  private static final String PASSWORD_REGEX = "^(?!\\d+$)(?=.*[A-Za-z])(?=.*\\d|.*[\\W_]).{10,}$";

  @Override
  public boolean isValid(String password, ConstraintValidatorContext context) {

    // 비밀번호가 null 인지 체크
    if (password == null) {
      context
          .buildConstraintViolationWithTemplate("비밀번호를 입력해주세요.")
          .addConstraintViolation()
          .disableDefaultConstraintViolation();
      return false;
    }

    // 비밀번호가 10자 이상인지 체크
    if (password.length() < 10) {
      context
          .buildConstraintViolationWithTemplate("비밀번호는 10자 이상이어야합니다.")
          .addConstraintViolation()
          .disableDefaultConstraintViolation();
      return false;
    }

    // 비밀번호가 정규식표현에 부합하는지 체크
    if (!password.matches(PASSWORD_REGEX)) {
      context
          .buildConstraintViolationWithTemplate("비밀번호는 숫자, 문자, 특수문자가 2가지 이상 포함되어야 합니다.")
          .addConstraintViolation()
          .disableDefaultConstraintViolation();
      return false;
    }

    return true;
  }
}
