package org.example.baba.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import org.example.baba.controller.dto.request.RegisterDTO;
import org.example.baba.domain.ApprovalCode;
import org.example.baba.domain.Register;
import org.example.baba.exception.CustomException;
import org.example.baba.exception.exceptionType.RegisterExceptionType;
import org.example.baba.repository.ApprovalCodeRepository;
import org.example.baba.repository.MemberRepository;
import org.example.baba.repository.RegisterRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class RegisterServiceTest {

  @Mock private RegisterRepository registerRepository;
  @Mock private MemberRepository memberRepository;
  @Mock private ApprovalCodeRepository approvalCodeRepository;

  @InjectMocks private MemberService memberService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  // existInDB 메소드 테스트

  @Test
  @DisplayName("DB에서 중복된 계정명 실패 케이스")
  public void duplicatedMemberNameInDB() {

    // given
    // RegisterDTO 생성
    RegisterDTO registerDTO =
        RegisterDTO.builder()
            .memberName("김민지")
            .password("Valid1234!")
            .email("minji12@gmail.com")
            .build();

    // existsByMemberName의 메소드 사용시 리턴값 설정
    when(memberRepository.existsByMemberName("김민지")).thenReturn(true);

    // when & then
    // memberService의 메소드 사용시 발생할 예외 클래스 확인
    CustomException thrown =
        assertThrows(
            CustomException.class,
            () -> {
              memberService.existInDB(registerDTO);
            });

    // 예외 메세지 확인
    assertEquals(RegisterExceptionType.DUPLICATED_MEMBER_NAME.getMessage(), thrown.getMessage());
  }

  @Test
  @DisplayName("DB에서 중복된 이메일 실패 케이스")
  public void duplicatedEmailInDB() {

    // given
    // RegisterDTO 생성
    RegisterDTO registerDTO =
        RegisterDTO.builder()
            .memberName("김성민")
            .password("Valid1234!")
            .email("minji@gmail.com")
            .build();

    // existsByMemberName의 메소드 사용시 리턴값 설정
    when(memberRepository.existsByEmail("minji@gmail.com")).thenReturn(true);

    // when & then
    // memberService의 메소드 사용시 발생할 예외 클래스 확인
    CustomException thrown =
        assertThrows(
            CustomException.class,
            () -> {
              memberService.existInDB(registerDTO);
            });

    // 예외 메세지 확인
    assertEquals(RegisterExceptionType.DUPLICATED_EMAIL.getMessage(), thrown.getMessage());
  }

  @Test
  @DisplayName("DB에서 중복되지 않은 계정명, 이메일 성공 케이스")
  public void noDuplicateNameAndEmailInDB() {
    // given
    // RegisterDTO 생성
    RegisterDTO registerDTO =
        RegisterDTO.builder()
            .memberName("김성민")
            .password("Valid1234!")
            .email("sungmin@gmail.com")
            .build();

    // existsByMemberName, existsByEmail 사용 시 리턴값 지정
    when(memberRepository.existsByMemberName("김성민")).thenReturn(false);
    when(memberRepository.existsByEmail("sungmin@gmail.com")).thenReturn(false);

    // when
    memberService.existInDB(registerDTO);

    // then
    // 아무런 예외가 발생하지 않아야 함
  }

  // existInRedis 메소드 테스트

  @Test
  @DisplayName("Redis 에서 중복된 계정명 실패 케이스")
  public void duplicatedMemberNameInRedis() {
    // given
    // RegisterDTO 생성
    RegisterDTO registerDTO =
        RegisterDTO.builder()
            .memberName("김민지")
            .password("Valid1234!")
            .email("minji12@gmail.com")
            .build();

    when(registerRepository.existsByMemberName("김민지")).thenReturn(true);

    // when & then
    CustomException thrown =
        assertThrows(CustomException.class, () -> memberService.existInRedis(registerDTO));
    // 예외 메세지 확인
    assertEquals(RegisterExceptionType.DUPLICATED_MEMBER_NAME.getMessage(), thrown.getMessage());
  }

  @Test
  @DisplayName("Redis 에서 중복된 이메일 실패 케이스")
  public void duplicatedEmailInRedis() {
    // given
    // RegisterDTO 생성
    RegisterDTO registerDTO =
        RegisterDTO.builder()
            .memberName("김민지")
            .password("Valid1234!")
            .email("minji12@gmail.com")
            .build();

    when(registerRepository.existsById(MemberService.MEMBERKEY_PREFIX + "minji12@gmail.com"))
        .thenReturn(true);

    // when & then
    CustomException thrown =
        assertThrows(CustomException.class, () -> memberService.existInRedis(registerDTO));
    // 예외 메세지 확인
    assertEquals(RegisterExceptionType.EMAIL_ALREADY_IN_PROGRESS.getMessage(), thrown.getMessage());
  }

  @Test
  @DisplayName("Redis 에서 중복되지 않은 계정명, 이메일 성공 케이스")
  public void noDuplicateNameAndEmailInRedis() {
    // given
    // RegisterDTO 생성
    RegisterDTO registerDTO =
        RegisterDTO.builder()
            .memberName("김민지")
            .password("Valid1234!")
            .email("minji12@gmail.com")
            .build();

    when(registerRepository.existsByMemberName("김민지")).thenReturn(true);
    when(registerRepository.existsById(MemberService.MEMBERKEY_PREFIX + "minji12@gmail.com"))
        .thenReturn(true);

    // when
    memberService.existInDB(registerDTO);

    // then
    // 아무런 예외가 발생하지 않아야 함
  }

  // sendApprovalCode 메소드 테스트

  @Test
  public void sendApprovalCodeTest() {
    // given
    RegisterDTO registerDTO =
        RegisterDTO.builder()
            .memberName("김민지")
            .password("Valid1234!")
            .email("minji12@gmail.com")
            .build();

    String fixedCode = "123456";

    MemberService spyMemberService = spy(memberService);
    doReturn(fixedCode).when(spyMemberService).generateRandomCode();

    Register expectedRegister = registerDTO.toEntity(MemberService.MEMBERKEY_PREFIX);
    ApprovalCode expectedApprovalCode =
        ApprovalCode.builder()
            .approvalKey(MemberService.APPROVALKEY_PREFIX + fixedCode)
            .email(registerDTO.getEmail())
            .build();

    // when
    spyMemberService.sendApprovalCode(registerDTO);

    // then
    verify(registerRepository).save(eq(expectedRegister));
    verify(approvalCodeRepository).save(eq(expectedApprovalCode));
  }
}
