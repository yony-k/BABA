package org.example.baba.controller.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import org.example.baba.common.anotation.ValidPassword;

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

  public String toJson() {
    try {
      return objectMapper.writeValueAsString(this);
    } catch (Exception e) {
      throw new RuntimeException("RegisterDTO 의 toJson 오류", e);
    }
  }

  public static RegisterDTO fromJson(String json) {
    try {
      return objectMapper.readValue(json, RegisterDTO.class);
    } catch (Exception e) {
      throw new RuntimeException("RegisterDTO 의 fromJson 오류", e);
    }
  }
}
