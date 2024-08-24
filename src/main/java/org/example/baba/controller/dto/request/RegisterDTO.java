package org.example.baba.controller.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import org.example.baba.common.anotation.ValidPassword;
import org.example.baba.domain.Register;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class RegisterDTO {

  private static final ObjectMapper objectMapper = new ObjectMapper();

  @NotBlank(message = "아이디를 입력해주세요.")
  String memberName;

  @NotBlank(message = "비밀번호를 입력해주세요.")
  @ValidPassword
  String password;

  @NotBlank(message = "이메일을 입력해주세요.")
  @Email(message = "올바른 이메일 형식이 아닙니다.")
  String email;

  public Register toEntity(String preFix) {
    return Register.builder()
        .email(preFix + this.email)
        .memberName(this.memberName)
        .password(this.password)
        .build();
  }
}
