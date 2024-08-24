package org.example.baba.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.util.Optional;

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

    // generateRandomCode 를 테스트 코드 쪽에서 수행하여 fixedCode 를 리턴하기 위해서 spy 사용
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

  // confirmApprovalCode 메소드 테스트

  @Test
  @DisplayName("가입승인 성공 케이스")
  public void confirmApprovalCodeSuccessTest() {
    // given
    String email = "minji12@gmail.com";
    String memberName = "김민지";
    String password = "Valid1234!";
    String Code = "123456";

    Register register =
        Register.builder()
            .email(MemberService.MEMBERKEY_PREFIX + email)
            .memberName(memberName)
            .password(password)
            .build();
    ApprovalCode approvalCode =
        ApprovalCode.builder()
            .approvalKey(MemberService.APPROVALKEY_PREFIX + Code)
            .email(email)
            .build();

    when(registerRepository.findById(MemberService.MEMBERKEY_PREFIX + email))
        .thenReturn(Optional.of(register));
    when(approvalCodeRepository.findById(MemberService.APPROVALKEY_PREFIX + Code))
        .thenReturn(Optional.of(approvalCode));

    // when
    memberService.confirmApprovalCode(Code);

    // then
    verify(memberRepository, times(1))
        .save(
            argThat(
                member ->
                    member.getEmail().equals(email)
                        && member.getMemberName().equals(memberName)
                        && member.getPassword().equals(password)));
    verify(registerRepository, times(1)).delete(eq(register));
    verify(approvalCodeRepository, times(1)).delete(eq(approvalCode));
  }

  @Test
  @DisplayName("가입승인 코드 만료 실패 케이스")
  public void expiredApproverCodeTest() {
    // given
    String code = "123456";

    when(approvalCodeRepository.findById(MemberService.APPROVALKEY_PREFIX + code))
        .thenReturn(Optional.empty());

    // when && then
    CustomException thrown =
        assertThrows(CustomException.class, () -> memberService.confirmApprovalCode(code));

    assertEquals(RegisterExceptionType.NOT_FOUND_CODE.getMessage(), thrown.getMessage());
    verify(registerRepository, never()).findById(any());
    verify(memberRepository, never()).save(any());
    verify(registerRepository, never()).delete(any());
    verify(approvalCodeRepository, never()).delete(any());
  }

  @Test
  @DisplayName("임시 사용자 정보 만료 실패 케이스")
  public void expiredMemberTest() {
    // given
    String email = "minji12@gmail.com";
    String code = "123456";

    ApprovalCode approvalCode =
        ApprovalCode.builder()
            .approvalKey(MemberService.APPROVALKEY_PREFIX + code)
            .email(email)
            .build();

    when(approvalCodeRepository.findById(MemberService.APPROVALKEY_PREFIX + code))
        .thenReturn(Optional.of(approvalCode));
    when(registerRepository.findById(MemberService.MEMBERKEY_PREFIX + email))
        .thenReturn(Optional.empty());

    // when && then
    CustomException thrown =
        assertThrows(CustomException.class, () -> memberService.confirmApprovalCode(code));

    assertEquals(RegisterExceptionType.NOT_FOUND_MEMBER.getMessage(), thrown.getMessage());
    verify(memberRepository, never()).save(any());
    verify(registerRepository, never()).delete(any());
    verify(approvalCodeRepository, never()).delete(any());
  }

  @Test
  @DisplayName("코드 발송한 이메일과 임시 저장된 이메일 불일치 실패 케이스")
  public void misMatchedEmailTest() {
    // given
    String email = "minji12@gmail.com";
    String mismatchedEmail = "sungmin12@gmail.com";
    String code = "123456";

    ApprovalCode approvalCode =
        ApprovalCode.builder()
            .approvalKey(MemberService.APPROVALKEY_PREFIX + code)
            .email(email)
            .build();

    Register register =
        Register.builder()
            .email(MemberService.MEMBERKEY_PREFIX + mismatchedEmail)
            .memberName("김민지")
            .password("Valid1234!")
            .build();

    when(approvalCodeRepository.findById(MemberService.APPROVALKEY_PREFIX + code))
        .thenReturn(Optional.of(approvalCode));
    when(registerRepository.findById(MemberService.MEMBERKEY_PREFIX + email))
        .thenReturn(Optional.of(register));

    // when & then
    CustomException thrown =
        assertThrows(CustomException.class, () -> memberService.confirmApprovalCode(code));

    assertEquals(RegisterExceptionType.DATA_CONSISTENCY_ERROR.message(), thrown.getMessage());
    verify(memberRepository, never()).save(any());
    verify(registerRepository, never()).delete(any());
    verify(approvalCodeRepository, never()).delete(any());
  }
}
