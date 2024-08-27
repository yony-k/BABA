package org.example.baba.common;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.example.baba.controller.dto.request.RegisterDTO;
import org.example.baba.domain.ApprovalCode;
import org.example.baba.domain.Register;
import org.example.baba.repository.ApprovalCodeRepository;
import org.example.baba.repository.RegisterRepository;
import org.example.baba.service.SafeStoreService;
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
  public void saveAndFindTest() {
    // given
    RegisterDTO registerDTO =
        RegisterDTO.builder()
            .memberName("김성민")
            .password("Valid1234!")
            .email("sungmin@gmail.com")
            .build();

    String email = SafeStoreService.MEMBERKEY_PREFIX + registerDTO.getEmail();
    String approvalKey = SafeStoreService.APPROVALKEY_PREFIX + "123456";

    Register expectedRegister = registerDTO.toEntity(SafeStoreService.MEMBERKEY_PREFIX);
    ApprovalCode expectedApprovalCode =
        ApprovalCode.builder().email(registerDTO.getEmail()).approvalKey(approvalKey).build();

    // when
    registerRepository.save(expectedRegister);
    approvalCodeRepository.save(expectedApprovalCode);

    // then
    Optional<Register> savedRegisterOptional = registerRepository.findById(email);
    Optional<ApprovalCode> savedApprovalCodeOptional = approvalCodeRepository.findById(approvalKey);

    assertTrue(savedRegisterOptional.isPresent(), "Register 데이터가 존재하지 않습니다.");
    assertTrue(savedApprovalCodeOptional.isPresent(), "ApprovalCode 데이터가 존재하지 않습니다.");

    assertEquals(expectedRegister, savedRegisterOptional.get(), "Register 일치하지 않음");
    assertEquals(expectedApprovalCode, savedApprovalCodeOptional.get(), "ApprovalCode 일치하지 않음");
  }

  @Test
  @DisplayName("Redis 데이터 삭제 테스트")
  public void deleteTest() {
    // given
    RegisterDTO registerDTO =
        RegisterDTO.builder()
            .memberName("김성민")
            .password("Valid1234!")
            .email("sungmin@gmail.com")
            .build();

    String email = SafeStoreService.MEMBERKEY_PREFIX + registerDTO.getEmail();
    String approvalKey = SafeStoreService.APPROVALKEY_PREFIX + "123456";

    Register expectedRegister = registerDTO.toEntity(SafeStoreService.MEMBERKEY_PREFIX);
    ApprovalCode expectedApprovalCode =
        ApprovalCode.builder().email(registerDTO.getEmail()).approvalKey(approvalKey).build();

    registerRepository.save(expectedRegister);
    approvalCodeRepository.save(expectedApprovalCode);

    // when
    registerRepository.delete(expectedRegister);
    approvalCodeRepository.delete(expectedApprovalCode);

    // then
    Optional<Register> savedRegister = registerRepository.findById(email);
    Optional<ApprovalCode> savedApprovalCode = approvalCodeRepository.findById(approvalKey);

    assertThat(savedRegister).isEmpty();
    assertThat(savedApprovalCode).isEmpty();
  }
}
