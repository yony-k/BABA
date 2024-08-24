package org.example.baba.common;

import static org.junit.jupiter.api.Assertions.*;

import org.example.baba.controller.dto.request.RegisterDTO;
import org.example.baba.domain.ApprovalCode;
import org.example.baba.domain.Register;
import org.example.baba.repository.ApprovalCodeRepository;
import org.example.baba.repository.RegisterRepository;
import org.example.baba.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RedisTest {

  @Autowired private RegisterRepository registerRepository;
  @Autowired private ApprovalCodeRepository approvalCodeRepository;

  @BeforeEach
  public void setUp() {
    registerRepository.deleteAll();
    approvalCodeRepository.deleteAll();
  }

  @Test
  @DisplayName("Redis 데이터 저장, 조회 테스트")
  public void redisRepositoryTest() {
    // given
    RegisterDTO registerDTO =
        RegisterDTO.builder()
            .memberName("김성민")
            .password("Valid1234!")
            .email("sungmin@gmail.com")
            .build();

    String email = MemberService.MEMBERKEY_PREFIX + registerDTO.getEmail();
    String approvalKey = MemberService.APPROVALKEY_PREFIX + "123456";

    Register expectedRegister = registerDTO.toEntity(MemberService.MEMBERKEY_PREFIX);
    ApprovalCode expectedApprovalCode =
        ApprovalCode.builder().email(registerDTO.getEmail()).approvalKey(approvalKey).build();

    // when
    registerRepository.save(expectedRegister);
    approvalCodeRepository.save(expectedApprovalCode);

    // then
    Register savedRegister = registerRepository.findById(email).get();
    ApprovalCode savedApprovalCode = approvalCodeRepository.findById(approvalKey).get();

    assertEquals(expectedRegister, savedRegister, "Register 일치하지 않음");
    assertEquals(expectedApprovalCode, savedApprovalCode, "ApprovalCode 일치하지 않음");
  }
}
