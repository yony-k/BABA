package org.example.baba.common;

import static org.junit.jupiter.api.Assertions.*;

import org.example.baba.controller.dto.request.RegisterDTO;
import org.example.baba.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
public class RedisTest {

  @Autowired private RedisTemplate redisTemplate;
  @Autowired private MemberService memberService;

  @BeforeEach
  public void setup() {
    redisTemplate.getConnectionFactory().getConnection();
  }

  @Test
  @DisplayName("Redis 데이터 저장, 조회 테스트")
  public void sendApprovalCode() {
    // given
    RegisterDTO registerDTO =
        RegisterDTO.builder()
            .memberName("김성민")
            .password("Valid1234!")
            .email("sungmin@gmail.com")
            .build();
    String memberKey = "temporary:" + registerDTO.getEmail();
    String approvalKey = "approval:" + "123456";

    // when
    memberService.sendApprovalCode(registerDTO);

    // then
    String stringRegisterDTO = (String) redisTemplate.opsForValue().get(memberKey);
    RegisterDTO savedRegisterDTO = RegisterDTO.fromJson(stringRegisterDTO);
    // String savedEmail = (String) redisTemplate.opsForValue().get(approvalKey);

    assertEquals(registerDTO, savedRegisterDTO, "registerDTO 일치하지 않음");
    // assertEquals(registerDTO.getEmail(), savedEmail, "email 일치하지 않음");
  }
}
