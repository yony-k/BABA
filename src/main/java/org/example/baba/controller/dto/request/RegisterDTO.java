package org.example.baba.controller.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import org.example.baba.common.anotation.ValidPassword;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class RegisterDTO {
  @NotBlank(message = "아이디를 입력하세요.")
  String username;

  @NotBlank(message = "비밀번호를 입력하세요.")
  @ValidPassword
  String password;

  @NotBlank(message = "이메일을 입려하세요.")
  @Email(message = "올바른 이메일 형식이 아닙니다.")
  String email;
}
