package org.example.baba.common.anotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

// 비밀번호 검증을 위한 커스텀 어노테이션
@Constraint(validatedBy = PasswordValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPassword {
  String message() default "비밀번호를 다시 입력해주세요.";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
