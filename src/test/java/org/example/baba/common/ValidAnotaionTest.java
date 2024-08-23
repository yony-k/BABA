package org.example.baba.common;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import org.example.baba.controller.dto.request.RegisterDTO;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ValidAnotaionTest {

  private static ValidatorFactory factory;
  private static Validator validator;

  @BeforeAll
  public static void setUp() {
    factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @AfterAll
  public static void tearDown() {
    factory.close();
  }

  @Test
  @DisplayName("형식에 맞는 비밀번호")
  public void validPassword() {
    // given
    RegisterDTO registerDTO = new RegisterDTO("smith12", "Valid1234!", "smith12@gmail.com");

    // when
    Set<ConstraintViolation<RegisterDTO>> result = validator.validate(registerDTO);

    // then
    assertTrue(result.isEmpty(), "비밀번호 유효성 검사가 실패했습니다.");
  }

  @Test
  @DisplayName("10자 미만인 비밀번호")
  public void inValidPasswordToShort() {
    // given
    RegisterDTO registerDTO = new RegisterDTO("smith12", "Valid123!", "smith12@gmail.com");

    // when
    Set<ConstraintViolation<RegisterDTO>> result = validator.validate(registerDTO);

    // then
    for (ConstraintViolation<RegisterDTO> violation : result) {
      assertTrue(violation.getMessage().contains("비밀번호는 10자 이상이어야합니다."), "메세지가 포함되어야합니다.");
    }
  }

  @Test
  @DisplayName("입력되지 않은 비밀번호")
  public void inValidPasswordNull() {
    // given
    RegisterDTO registerDTO = new RegisterDTO("smith12", null, "smith12@gmail.com");

    // when
    Set<ConstraintViolation<RegisterDTO>> result = validator.validate(registerDTO);

    // then
    for (ConstraintViolation<RegisterDTO> violation : result) {
      assertTrue(violation.getMessage().contains("비밀번호를 입력해주세요."), "메세지가 포함되어야합니다.");
    }
  }

  @Test
  @DisplayName("정규식에 부합하지 않는 비밀번호")
  public void inValidPasswordRegexp() {
    // given
    RegisterDTO registerDTO = new RegisterDTO("smith12", "Valid12345", "smith12@gmail.com");

    // when
    Set<ConstraintViolation<RegisterDTO>> result = validator.validate(registerDTO);

    // then
    for (ConstraintViolation<RegisterDTO> violation : result) {
      assertTrue(
          violation.getMessage().contains("비밀번호는 숫자, 문자, 특수문자가 2가지 이상 포함되어야 합니다."),
          "메세지가 포함되어야합니다.");
    }
  }
}
